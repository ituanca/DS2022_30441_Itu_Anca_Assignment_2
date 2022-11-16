package ro.tuc.ds2022.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.tuc.ds2022.dtos.HourlyEnergyConsumptionDTO;
import ro.tuc.ds2022.services.HourlyEnergyConsumptionService;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/hourlyEnergyConsumption")
public class HourlyEnergyConsumptionController {

    private final HourlyEnergyConsumptionService hourlyEnergyConsumptionService;

    @Autowired
    public HourlyEnergyConsumptionController(HourlyEnergyConsumptionService hourlyEnergyConsumptionService) {
        this.hourlyEnergyConsumptionService = hourlyEnergyConsumptionService;
    }

    @GetMapping("/energyConsumption")
    public List<Double> findEnergyConsumptionValues
            (@Param("deviceName") String deviceName, @Param("date") String date) throws ParseException {
        return hourlyEnergyConsumptionService.findEnergyConsumptionValues(deviceName, date);
    }

}
