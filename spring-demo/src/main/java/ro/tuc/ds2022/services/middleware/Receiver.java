package ro.tuc.ds2022.services.middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;
import ro.tuc.ds2022.measurements.Measurement;
import ro.tuc.ds2022.services.HourlyEnergyConsumptionService;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class Receiver {

    String delimiters = ", ";
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    private final HourlyEnergyConsumptionService hourlyEnergyConsumptionService;

    public Receiver(HourlyEnergyConsumptionService hourlyEnergyConsumptionService) {
        this.hourlyEnergyConsumptionService = hourlyEnergyConsumptionService;
    }

    @RabbitListener(queues = ReceiverConfig.QUEUE_NAME)
    public void receiveMessage(List<String> message) throws ParseException {
        logger.info("Received message: " +  message);
        List<Measurement> measurements = createArrayOfMeasurements(message);
        hourlyEnergyConsumptionService.insertEnergyConsumptionValues(measurements);
        hourlyEnergyConsumptionService.checkIfLimitExceeded(measurements);
    }

    private List<Measurement> createArrayOfMeasurements(List<String> measurementsStringList){
        List<Measurement> measurements = new ArrayList<>();
        for(String measurementString : measurementsStringList){
            Measurement measurement = convertStringToMeasurementObject(measurementString);
            measurements.add(measurement);
        }
        return measurements;
    }

    private Measurement convertStringToMeasurementObject(String measurementString){
        String[] array = measurementString.split(delimiters);
        Measurement measurement = new Measurement();
        measurement.setTimestamp(LocalDateTime.parse(array[0]));
        measurement.setDeviceId(Integer.parseInt(array[1]));
        measurement.setEnergyConsumption(Double.parseDouble(array[2]));
        return measurement;
    }

}
