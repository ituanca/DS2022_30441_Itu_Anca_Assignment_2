package ro.tuc.messageproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import ro.tuc.messageproducer.devices.DeviceService;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@ComponentScan("ro.tuc.messageproducer")
public class MessageProducerApplication extends SpringBootServletInitializer {

	public static final String EXCHANGE_NAME = "appExchange";
	public static final String ROUTING_KEY = "messages.key";

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(MessageProducerApplication.class, args);
	}

}
