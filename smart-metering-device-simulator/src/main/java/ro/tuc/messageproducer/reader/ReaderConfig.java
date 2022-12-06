package ro.tuc.messageproducer.reader;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReaderConfig {

    public static final String EXCHANGE_NAME = "appExchange";
    public static final String QUEUE_NAME = "appQueue";
    public static final String ROUTING_KEY = "messages.key";

    @Bean
    public Reader getReader(){
        return new Reader();
    }

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue appQueueSpecific() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding declareBindingSpecific() {
        return BindingBuilder.bind(appQueueSpecific()).to(appExchange()).with(ROUTING_KEY);
    }

}
