package ro.tuc.ds2022.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "/webSocket")
public class WebSocketController {

    @MessageMapping("/news")
    @SendTo("/topic/news")
    public String broadcastNews(@Payload String message){
        return message;
    }

}
