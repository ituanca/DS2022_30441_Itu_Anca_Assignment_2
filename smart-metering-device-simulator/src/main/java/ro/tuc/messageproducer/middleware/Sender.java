package ro.tuc.messageproducer.middleware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.tuc.messageproducer.MessageProducerApplication;

@Slf4j
@Service
public class Sender {

    int i = 0;
    private final RabbitTemplate rabbitTemplate;

    public Sender(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 600000L)  // 10 minutes
    public void sendMessage() {
        final String message = "Hello there " + (i++);
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(
                MessageProducerApplication.EXCHANGE_NAME,
                MessageProducerApplication.ROUTING_KEY,
                message
        );
    }
}
