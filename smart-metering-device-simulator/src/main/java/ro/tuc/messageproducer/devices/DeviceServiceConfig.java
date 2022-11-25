package ro.tuc.messageproducer.devices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceServiceConfig {

    @Bean
    public DeviceService getDeviceService() {
        return new DeviceService();
    }

}
