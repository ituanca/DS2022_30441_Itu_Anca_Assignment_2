package ro.tuc.ds2022.services.middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ro.tuc.ds2022.Ds2022Application;

@Service
public class Receiver {

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    @RabbitListener(queues = Ds2022Application.QUEUE_SPECIFIC_NAME)
    public void receiveMessage(String message) {
        logger.info("Received message: "+  message);
    }

}
