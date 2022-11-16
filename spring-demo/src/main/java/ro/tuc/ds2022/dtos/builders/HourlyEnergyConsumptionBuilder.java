package ro.tuc.ds2022.dtos.builders;

import ro.tuc.ds2022.dtos.HourlyEnergyConsumptionDTO;
import ro.tuc.ds2022.entities.HourlyEnergyConsumption;

public class HourlyEnergyConsumptionBuilder {

    public static HourlyEnergyConsumptionDTO toHourlyEnergyConsumptionDTOWithoutDevice(HourlyEnergyConsumption hourlyEnergyConsumption){
        return new HourlyEnergyConsumptionDTO(
                hourlyEnergyConsumption.getId(),
                hourlyEnergyConsumption.getTimestamp(),
                hourlyEnergyConsumption.getEnergyConsumption());
    }

    public static HourlyEnergyConsumptionDTO toHourlyEnergyConsumptionDTO(HourlyEnergyConsumption hourlyEnergyConsumption){
        return new HourlyEnergyConsumptionDTO(
                hourlyEnergyConsumption.getId(),
                DeviceBuilder.toDeviceDTOWithoutOwnerWithoutConsumption(hourlyEnergyConsumption.getDevice()),
                hourlyEnergyConsumption.getTimestamp(),
                hourlyEnergyConsumption.getEnergyConsumption());
    }
}
