package ro.tuc.ds2022.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2022.services.HourlyEnergyConsumptionService;

@RestController
@CrossOrigin
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private HourlyEnergyConsumptionService hourlyEnergyConsumptionService;

    public WebSocketController(HourlyEnergyConsumptionService hourlyEnergyConsumptionService) {
        this.hourlyEnergyConsumptionService = hourlyEnergyConsumptionService;
    }

    @MessageMapping("/private-message")
    public String receiveSignalAndSendNotification(@Payload String message) {
        String username = message;
        if(hourlyEnergyConsumptionService.checkIfLimitExceededForUser(username)){
            message = hourlyEnergyConsumptionService.generateMessageForLimitExceeded(username);
        }
        simpMessagingTemplate.convertAndSendToUser(username, "/private", message); // /user/anca/private
        return message;
    }

}
