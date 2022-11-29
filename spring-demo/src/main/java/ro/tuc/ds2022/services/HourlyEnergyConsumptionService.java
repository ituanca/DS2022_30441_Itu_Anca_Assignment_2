package ro.tuc.ds2022.services;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2022.entities.Device;
import ro.tuc.ds2022.entities.HourlyEnergyConsumption;
import ro.tuc.ds2022.measurements.Measurement;
import ro.tuc.ds2022.repositories.HourlyEnergyConsumptionRepository;
import ro.tuc.ds2022.services.middleware.Receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class HourlyEnergyConsumptionService {

    private static final Logger logger = LoggerFactory.getLogger(HourlyEnergyConsumptionService.class);
    private final HourlyEnergyConsumptionRepository hourlyEnergyConsumptionRepository;
    private final DeviceService deviceService;
    private final PersonService personService;

    @Autowired
    public HourlyEnergyConsumptionService(HourlyEnergyConsumptionRepository hourlyEnergyConsumptionRepository, DeviceService deviceService, PersonService personService) {
        this.hourlyEnergyConsumptionRepository = hourlyEnergyConsumptionRepository;
        this.deviceService = deviceService;
        this.personService = personService;
    }

    public List<HourlyEnergyConsumption> findEnergyConsumption(String deviceName, String dateString) throws ParseException {
        Device device = deviceService.findDeviceByName(deviceName);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        return hourlyEnergyConsumptionRepository.findByDateAndDevice(date, device);
    }

    public List<Double> findEnergyConsumptionValues(String deviceName, String dateString) throws ParseException {
        List<HourlyEnergyConsumption> hourlyEnergyConsumption = findEnergyConsumption(deviceName, dateString);
        List<Double> listOfEnergyConsumptionByHour = new ArrayList<>();
        for(int i = 0; i < 24; i++) {
            boolean added = false;
            for (HourlyEnergyConsumption h : hourlyEnergyConsumption) {
                if (h.getTimestamp().getHour() == i) {
                    listOfEnergyConsumptionByHour.add(h.getEnergyConsumption());
                    added = true;
                }
            }
            if(!added){
                listOfEnergyConsumptionByHour.add((double) 0);
            }
        }
        return listOfEnergyConsumptionByHour;
    }

//    private Double generateRandomEnergyConsumptionForDevice(Device device, Random random){
//        return ThreadLocalRandom.current().nextDouble(0,device.getMaxHourlyEnergyConsumption());
//    }
//
//    private LocalDateTime generateRandomTimestamp(Month month, int maxDay){
//        int minDay = 1;
//        int minHour = 0;
//        int maxHour = 23;
//        int randomDay = ThreadLocalRandom.current().nextInt(minDay, maxDay);
//        int randomHour = ThreadLocalRandom.current().nextInt(minHour, maxHour);
//        return LocalDateTime.of(2022, month, randomDay, randomHour, 0, 0);
//    }

    public void insertEnergyConsumptionValues(List<Measurement> measurements){
       for(Measurement measurement : measurements){
           HourlyEnergyConsumption hourlyEnergyConsumption =
                            new HourlyEnergyConsumption(
                                    deviceService.findDeviceById(measurement.getDeviceId()),
                                    measurement.getTimestamp(),
                                    measurement.getEnergyConsumption());
           if(!hourlyEnergyConsumptionRepository.findByTimestampAndDevice(
                                    hourlyEnergyConsumption.getTimestamp(),
                                    hourlyEnergyConsumption.getDevice())
                            .isPresent()){
                        hourlyEnergyConsumptionRepository.save(hourlyEnergyConsumption);
                    }
       }
    }

//    @PostConstruct
//    private void addValues(){
//        List<Device> devices = deviceService.findDeviceByOwner(personService.findPersonByUsername("anca").orElse(null));
//        Random random = new Random();
//        if(!devices.isEmpty()){
//            for(int i=0; i<5; i++) {
//                for(Device device : devices){
//                    HourlyEnergyConsumption hourlyEnergyConsumption =
//                            new HourlyEnergyConsumption(
//                                    device,
//                                    generateRandomTimestamp(Month.NOVEMBER, 10),
//                                    generateRandomEnergyConsumptionForDevice(device, random));
//                    if(!hourlyEnergyConsumptionRepository.findByTimestampAndDevice(
//                                    hourlyEnergyConsumption.getTimestamp(),
//                                    hourlyEnergyConsumption.getDevice())
//                            .isPresent()){
//                        hourlyEnergyConsumptionRepository.save(hourlyEnergyConsumption);
//                    }
//                }
//            }
//        }
//    }

    public void checkIfLimitExceeded(List<Measurement> measurements) throws ParseException {
        for(Measurement measurement : measurements){
            double totalAtSpecifiedHour = computeTotalEnergyConsumptionAtSpecifiedHour(measurement);
            Device device = deviceService.findDeviceById(measurement.getDeviceId());
            boolean limitExceeded = totalAtSpecifiedHour > device.getMaxHourlyEnergyConsumption();
            if(limitExceeded){
                System.out.println("EXCEEDED!!!! - " + device.getName() + " max accepted value: " + device.getMaxHourlyEnergyConsumption() + ", registered value: " + totalAtSpecifiedHour);
            }
        }
    }

    private Double computeTotalEnergyConsumptionAtSpecifiedHour(Measurement measurement) throws ParseException {
        List<HourlyEnergyConsumption> registeredHourlyEnergyConsumption =
                findEnergyConsumptionForDeviceByDateAndHour(measurement.getDeviceId(), measurement.getTimestamp());
        double totalHourlyEnergyConsumption = 0;
        for(HourlyEnergyConsumption registeredValue : registeredHourlyEnergyConsumption){
            totalHourlyEnergyConsumption = totalHourlyEnergyConsumption + registeredValue.getEnergyConsumption();
        }
        logger.info(String.valueOf(totalHourlyEnergyConsumption));
        return totalHourlyEnergyConsumption;
    }

    private List<HourlyEnergyConsumption> findEnergyConsumptionForDeviceByDateAndHour(Integer deviceId, LocalDateTime timestamp) throws ParseException {
        Device device = deviceService.findDeviceById(deviceId);
        Date date = Date.from(timestamp.atZone(ZoneId.of("Europe/Bucharest")).toInstant());
        Integer hour = timestamp.getHour();
        return hourlyEnergyConsumptionRepository.findByDateAndHourAndDevice(date, hour, device);
    }
}
