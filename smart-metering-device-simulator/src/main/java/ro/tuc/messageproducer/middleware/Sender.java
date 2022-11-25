package ro.tuc.messageproducer.middleware;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.tuc.messageproducer.MessageProducerApplication;
import ro.tuc.messageproducer.devices.Device;
import ro.tuc.messageproducer.devices.DeviceService;
import ro.tuc.messageproducer.measurements.Measurement;
import ro.tuc.messageproducer.reader.Reader;

import java.io.IOException;

@Slf4j
@Service
public class Sender {

    private final RabbitTemplate rabbitTemplate;
    //Double energyConsumptionValue;

    @Autowired
    private Reader reader;

    @Autowired
    private DeviceService deviceService;

    public Sender(final RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

//    @Scheduled(fixedDelay = 600000L)  // 10 minutes
    @Scheduled(fixedDelay = 10000L)  // 10 seconds
    public void sendMessage() throws IOException {
        List<Device> devices = getDevices();
        List<String> message = new ArrayList<>();
        for(Device d: devices){
            Double energyConsumptionValue = readFromFile();
            Measurement measurement = new Measurement();
            measurement.setDeviceId(d.getId());
            measurement.setEnergyConsumption(energyConsumptionValue);
            message.add(measurement.toString());
        }

        log.info("Sending message...");
        rabbitTemplate.convertAndSend(
                MessageProducerApplication.EXCHANGE_NAME,
                MessageProducerApplication.ROUTING_KEY,
                message
        );
    }

    public Double readFromFile() throws IOException{
        return reader.readFromFile();
    }

    private List<Device> getDevices(){
        return deviceService.getDevices();
    }

}
