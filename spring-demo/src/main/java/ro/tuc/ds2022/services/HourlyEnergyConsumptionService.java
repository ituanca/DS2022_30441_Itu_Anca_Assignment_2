package ro.tuc.ds2022.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2022.entities.Device;
import ro.tuc.ds2022.entities.HourlyEnergyConsumption;
import ro.tuc.ds2022.entities.Person;
import ro.tuc.ds2022.measurements.Measurement;
import ro.tuc.ds2022.repositories.HourlyEnergyConsumptionRepository;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public void insertEnergyConsumptionValues(List<Measurement> measurements) throws ParseException {
       if(!measurements.isEmpty()){
           for(Measurement measurement : measurements){
               HourlyEnergyConsumption registeredEnergyConsumptionForDeviceByDateAndHour =
                       findEnergyConsumptionForDeviceByDateAndHour(measurement.getDeviceId(), measurement.getTimestamp());
               if(registeredEnergyConsumptionForDeviceByDateAndHour!=null) { // if there already exists a value registered for that hour
                   registeredEnergyConsumptionForDeviceByDateAndHour.setTimestamp(measurement.getTimestamp());
                   registeredEnergyConsumptionForDeviceByDateAndHour.setEnergyConsumption(measurement.getEnergyConsumption());
                   hourlyEnergyConsumptionRepository.save(registeredEnergyConsumptionForDeviceByDateAndHour);
               }else{
                   HourlyEnergyConsumption hourlyEnergyConsumption =
                           new HourlyEnergyConsumption(
                                   deviceService.findDeviceById(measurement.getDeviceId()),
                                   measurement.getTimestamp(),
                                   measurement.getEnergyConsumption());
                   hourlyEnergyConsumptionRepository.save(hourlyEnergyConsumption);
               }
           }
       }
    }

    public void insertEnergyConsumptionValue(Measurement measurement) throws ParseException {
        HourlyEnergyConsumption registeredEnergyConsumptionForDeviceByDateAndHour =
                findEnergyConsumptionForDeviceByDateAndHour(measurement.getDeviceId(), measurement.getTimestamp());
        if(registeredEnergyConsumptionForDeviceByDateAndHour!=null) { // if there already exists a value registered for that hour
            registeredEnergyConsumptionForDeviceByDateAndHour.setTimestamp(measurement.getTimestamp());
            registeredEnergyConsumptionForDeviceByDateAndHour.setEnergyConsumption(measurement.getEnergyConsumption());
            hourlyEnergyConsumptionRepository.save(registeredEnergyConsumptionForDeviceByDateAndHour);
        }else{
            HourlyEnergyConsumption hourlyEnergyConsumption =
                    new HourlyEnergyConsumption(
                            deviceService.findDeviceById(measurement.getDeviceId()),
                            measurement.getTimestamp(),
                            measurement.getEnergyConsumption());
            hourlyEnergyConsumptionRepository.save(hourlyEnergyConsumption);
        }
    }

    private HourlyEnergyConsumption findEnergyConsumptionForDeviceByDateAndHour(Integer deviceId, LocalDateTime timestamp) {
        Device device = deviceService.findDeviceById(deviceId);
        LocalDate date = timestamp.toLocalDate();
        Integer hour = timestamp.getHour();
        return hourlyEnergyConsumptionRepository.findByDateAndHourAndDevice(date, hour, device);
    }

    public Boolean checkIfLimitExceededForUser(String username) {
        Person person = personService.findPersonByUsername(username).orElse(null);
        if(person!=null){
            List<Device> devices = person.getDevices();
            for(Device device : devices){
                List<HourlyEnergyConsumption> listOfRegisteredHourlyEnergyConsumption =
                        hourlyEnergyConsumptionRepository.findByDevice(device);
                for(HourlyEnergyConsumption consumption : listOfRegisteredHourlyEnergyConsumption){
                    double totalAtSpecifiedHour = consumption.getEnergyConsumption();
                    boolean limitExceeded = totalAtSpecifiedHour > device.getMaxHourlyEnergyConsumption();
                    if(limitExceeded){
                        logger.info("THRESHOLD EXCEEDED!!!! - " + device.getName());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String generateMessageForLimitExceeded(String username){
        Person person = personService.findPersonByUsername(username).orElse(null);
        StringBuilder message = new StringBuilder();
        message.append(username).append(",").append("***");
        if(person!=null){
            List<Device> devices = person.getDevices();
            for(Device device : devices){
                List<HourlyEnergyConsumption> listOfRegisteredHourlyEnergyConsumption =
                        hourlyEnergyConsumptionRepository.findByDevice(device);
                for(HourlyEnergyConsumption consumption : listOfRegisteredHourlyEnergyConsumption){
                    double totalAtSpecifiedHour = consumption.getEnergyConsumption();
                    boolean limitExceeded = totalAtSpecifiedHour > device.getMaxHourlyEnergyConsumption();
                    if(limitExceeded){
                        message.append("your device ")
                                .append(device.getName())
                                .append(" consumed a quantity of ")
                                .append(totalAtSpecifiedHour)
                                .append("W on ").append(consumption.getTimestamp().toLocalDate())
                                .append(" at ").append(consumption.getTimestamp().toLocalTime())
                                .append(". The maximum accepted hourly energy consumption for this device is ")
                                .append(device.getMaxHourlyEnergyConsumption())
                                .append("!").append("***");
                        break;
                    }
                }
            }
        }
        return message.toString();
    }
}
