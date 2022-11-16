package ro.tuc.ds2022.dtos.builders;

import ro.tuc.ds2022.dtos.DeviceDTO;
import ro.tuc.ds2022.dtos.HourlyEnergyConsumptionDTO;
import ro.tuc.ds2022.entities.Device;
import ro.tuc.ds2022.entities.HourlyEnergyConsumption;

import java.util.ArrayList;
import java.util.List;

public class DeviceBuilder {

    public static DeviceDTO toDeviceDTO(Device device){
        DeviceDTO dto = new DeviceDTO(device.getId(), device.getName(), device.getDescription(), device.getAddress(),
                    device.getMaxHourlyEnergyConsumption());
        if(device.getOwner()!=null){
            dto.setOwner(PersonBuilder.toPersonDetailsDTO(device.getOwner()));
        }
        // don't send to frontend the hourlyConsumption registrations together with the attribute devices (otherwise -> infinite loop)
        if(!device.getListHourlyConsumption().isEmpty()){
            List<HourlyEnergyConsumptionDTO> listHourlyConsumption = new ArrayList<>();
            for(HourlyEnergyConsumption h: device.getListHourlyConsumption()){
                listHourlyConsumption.add(HourlyEnergyConsumptionBuilder.toHourlyEnergyConsumptionDTOWithoutDevice(h));
            }
            dto.setHourlyConsumption(listHourlyConsumption);
        }
        return dto;
    }

    public static DeviceDTO toDeviceDTOWithoutConsumption(Device device){
        DeviceDTO dto = new DeviceDTO(device.getId(), device.getName(), device.getDescription(),
                device.getAddress(), device.getMaxHourlyEnergyConsumption());
        if(device.getOwner()!=null){
            dto.setOwner(PersonBuilder.toPersonDetailsDTO(device.getOwner()));
        }
        return dto;
    }

    public static DeviceDTO toDeviceDTOWithoutOwnerWithoutConsumption(Device device){
        return new DeviceDTO(device.getId(), device.getName(), device.getDescription(),
                device.getAddress(), device.getMaxHourlyEnergyConsumption());
    }

    public static Device toEntity(DeviceDTO deviceDTO){
        return new Device(deviceDTO.getName(), deviceDTO.getDescription(), deviceDTO.getAddress(),
                deviceDTO.getMaxHourlyEnergyConsumption());
    }

}
