package ro.tuc.ds2022.services.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;
import ro.tuc.ds2022.measurements.Measurement;
import ro.tuc.ds2022.services.HourlyEnergyConsumptionService;

import java.time.LocalDateTime;

@Service
public class Receiver {

    String delimiters = ", ";
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    private final HourlyEnergyConsumptionService hourlyEnergyConsumptionService;

    public Receiver(HourlyEnergyConsumptionService hourlyEnergyConsumptionService) {
        this.hourlyEnergyConsumptionService = hourlyEnergyConsumptionService;
    }

    @RabbitListener(queues = ReceiverConfig.QUEUE_NAME)
    public void receiveMessage(String message) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqps://cfmlgvxp:DU0-aflUpbKtnLJYr2pxgvyf3IhMqYCc@sparrow.rmq.cloudamqp.com/cfmlgvxp");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(ReceiverConfig.QUEUE_NAME, true, false, false, null);

        logger.info("Received message: " +  message);
        Measurement measurement = convertStringToMeasurementObject(message);
        System.out.println(measurement.getTimestamp() + " " + measurement.getDeviceId() + " " + measurement.getEnergyConsumption());
        hourlyEnergyConsumptionService.insertEnergyConsumptionValue(measurement);
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
