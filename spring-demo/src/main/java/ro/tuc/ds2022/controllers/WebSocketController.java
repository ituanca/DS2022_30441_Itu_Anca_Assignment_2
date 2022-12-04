package ro.tuc.ds2022.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public String sendNotificationToUser(@Payload String message){
        String username = "anca";
        message = "this is the message";
        simpMessagingTemplate.convertAndSendToUser(username, "/private", message); // /user/anca/private
        return message;
    }

    @SendTo("/topic/news")
    public String broadcastNews(@Payload String message) {
        return message;
    }

}
