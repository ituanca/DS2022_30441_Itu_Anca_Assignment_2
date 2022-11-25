package ro.tuc.messageproducer.reader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReaderConfig {

    @Bean
    public Reader getReader(){
        return new Reader();
    }

}
