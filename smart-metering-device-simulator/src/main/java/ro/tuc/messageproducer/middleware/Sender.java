package ro.tuc.messageproducer.middleware;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.tuc.messageproducer.devices.Device;
import ro.tuc.messageproducer.devices.DeviceService;
import ro.tuc.messageproducer.measurements.Measurement;
import ro.tuc.messageproducer.reader.Reader;
import ro.tuc.messageproducer.reader.ReaderConfig;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class Sender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private Reader reader;

    @Autowired
    private DeviceService deviceService;

    public Sender(final RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    //@Scheduled(fixedDelay = 600000L)  // 10 minutes
    //@Scheduled(fixedDelay = 10000L)  // 10 seconds
    //@Scheduled(fixedDelay = 60000L)  // 1 minute
    public void composeAndSendMessage() throws IOException {
        List<Device> devices = getDevices();
        List<String> message = new ArrayList<>();
        Double energyConsumptionValue = readFromFile();
        for(Device d: devices){
            Measurement measurement = new Measurement(
                    LocalDateTime.now(ZoneId.of("Europe/Bucharest")),
                    d.getId(),
                    energyConsumptionValue
            );
            message.add(measurement.toString());
        }
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(
                ReaderConfig.EXCHANGE_NAME,
                ReaderConfig.ROUTING_KEY,
                message
        );
    }

    //@Scheduled(fixedDelay = 60000L)  // 1 minute
    public void composeAndSendMessageForSingleDevice() throws IOException {
        Device device = getDevice(readIdFromConfigFile());
        List<String> message = new ArrayList<>();
        Double energyConsumptionValue = readFromFileSingleDevice();
        Measurement measurement = new Measurement(
                LocalDateTime.now(ZoneId.of("Europe/Bucharest")), device.getId(), energyConsumptionValue
        );
        message.add(measurement.toString());

        log.info("Sending message...");
        rabbitTemplate.convertAndSend(
                ReaderConfig.EXCHANGE_NAME,
                ReaderConfig.ROUTING_KEY,
                message
        );
    }

    @Scheduled(fixedDelay = 60000L)  // 1 minute
    public void composeAndSendMessageTest() throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException, TimeoutException {
        Integer deviceId = readIdFromConfigFile();
        Double energyConsumptionValue = readFromFileSingleDevice();
        Measurement measurement = new Measurement(
                LocalDateTime.now(ZoneId.of("Europe/Bucharest")), deviceId, energyConsumptionValue
        );
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqps://cfmlgvxp:DU0-aflUpbKtnLJYr2pxgvyf3IhMqYCc@sparrow.rmq.cloudamqp.com/cfmlgvxp");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(ReaderConfig.QUEUE_NAME, true, false, false, null);
        channel.basicPublish("", ReaderConfig.QUEUE_NAME, false, null, measurement.toString().getBytes());
        System.out.println("measurement sent: " + measurement);
        connection.close();
    }

    private Double readFromFile() throws IOException{
        return reader.readFromFile();
    }

    private List<Device> getDevices(){
        return deviceService.getDevices();
    }

    private Double readFromFileSingleDevice() throws IOException{
        return reader.readFromFileSingleDevice();
    }

    private Device getDevice(Integer deviceId){
        return deviceService.getDeviceById(deviceId);
    }

    private Integer readIdFromConfigFile() throws IOException {
        String configFilePath = "src/main/resources/sensor-files/config.properties";
        FileInputStream propInput = new FileInputStream(configFilePath);
        Properties prop = new Properties();
        prop.load(propInput);
        return Integer.parseInt(prop.getProperty("DEVICE_ID"));
    }


}
