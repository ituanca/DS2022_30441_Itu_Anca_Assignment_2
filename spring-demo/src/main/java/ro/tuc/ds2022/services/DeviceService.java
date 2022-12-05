package ro.tuc.ds2022.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2022.dtos.DeviceDTO;
import ro.tuc.ds2022.dtos.builders.DeviceBuilder;
import ro.tuc.ds2022.entities.Device;
import ro.tuc.ds2022.wrappers.DeviceList;
import ro.tuc.ds2022.entities.Person;
import ro.tuc.ds2022.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2022.repositories.DeviceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final PersonService personService;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, PersonService personService) {
        this.deviceRepository = deviceRepository;
        this.personService = personService;
    }

    public List<DeviceDTO> findListDeviceDTO() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTOWithoutConsumption)
                .collect(Collectors.toList());
    }

    public List<Device> findDevices() {
        return deviceRepository.findAll();
    }

    public DeviceDTO findDeviceDTOById(Integer id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            log.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDTO(deviceOptional.get());
    }

    public Device findDeviceById(Integer id) {
        Device device = deviceRepository.findById(id).orElse(null);
        if(device!=null){
            device.setOwner(null);
            device.setListHourlyConsumption(null);
        }
        return device;
    }

    public Device findDeviceByName(String name) {
        Optional<Device> deviceOptional = deviceRepository.findByName(name);
        if (!deviceOptional.isPresent()) {
            log.error("Device with name {} was not found in db", name);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + name);
        }
        return deviceOptional.get();
    }

    public String insert(DeviceDTO deviceDTO) {
        if(checkIfNameExists(deviceDTO.getName())){
            return "name_error";
        }
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        log.info("Device with id {} was inserted in db", device.getId());
        return "ok";
    }

    public boolean checkIfNameExists(String name) {
        Optional<Device> deviceOptional = deviceRepository.findByName(name);
        if (!deviceOptional.isPresent()) {
            log.info("Name {} was not found in db", name);
        }
        return deviceOptional.isPresent();
    }

    public String update(DeviceDTO deviceDTO) {
        Device device = deviceRepository.findById(deviceDTO.getId()).orElse(null);
        if(device!=null){
            if(!device.getName().equals(deviceDTO.getName())){ // if name was updated
                if(checkIfNameExists(deviceDTO.getName())){
                    return "name_error";
                }
            }
            device.setName(deviceDTO.getName());
            device.setDescription(deviceDTO.getDescription());
            device.setAddress(deviceDTO.getAddress());
            device.setMaxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption());
            deviceRepository.save(device);
            log.debug("Device with id {} was inserted in db", device.getId());
            return "ok";
        }
        return "error";
    }

    public String delete(String name) {
        Device device = deviceRepository.findByName(name).orElse(null);
        if(device!=null){
            if(device.getOwner()!=null){
                device.setOwner(null);
                deviceRepository.save(device);
            }
            deviceRepository.delete(device);
            log.debug("Device with id {} was deleted from db", device.getId());
            return "ok";
        }
        return "error";
    }

    public List<DeviceDTO> findDevicesDTOWithoutOwner() {
        List<Device> devices = deviceRepository.findAll();
        List<Device> devicesWithoutOwner = new ArrayList<>();
        for(Device d : devices){
            if(d.getOwner()==null){
                devicesWithoutOwner.add(d);
            }
        }
        return devicesWithoutOwner.stream()
                .map(DeviceBuilder::toDeviceDTOWithoutConsumption)
                .collect(Collectors.toList());
    }

    public List<DeviceDTO> findDevicesDTOWithOwner() {
        List<Device> devices = deviceRepository.findAll();
        List<Device> devicesWithOwner = new ArrayList<>();
        for(Device d : devices){
            if(d.getOwner()!=null){
                devicesWithOwner.add(d);
            }
        }
        return devicesWithOwner.stream()
                .map(DeviceBuilder::toDeviceDTOWithoutConsumption)
                .collect(Collectors.toList());
    }

    public DeviceList findDeviceListWithOwner() {
        List<Device> devices = deviceRepository.findAll();
        List<Device> devicesWithOwner = new ArrayList<>();
        for(Device d : devices){
            if(d.getOwner()!=null){
                devicesWithOwner.add(d);
            }
        }
        DeviceList deviceListObject = new DeviceList();
        deviceListObject.setDevices(devicesWithOwner);
        for(Device d: deviceListObject.getDevices()){
            d.setOwner(null);
            d.setListHourlyConsumption(null);
        }
        return deviceListObject;
    }

    public String createAssociation(String deviceName, String ownerUsername){
        Device device = deviceRepository.findByName(deviceName).orElse(null);
        Person owner = personService.findPersonByUsername(ownerUsername).orElse(null);
        if(device != null && owner != null){
            device.setOwner(owner);
            deviceRepository.save(device);
            return "ok";
        }
        return "error";
    }

    public String updateAssociation(String deviceName, String ownerUsername) {
        Device device = deviceRepository.findByName(deviceName).orElse(null);
        Person owner = personService.findPersonByUsername(ownerUsername).orElse(null);
        if(device != null && owner != null){
            device.setOwner(owner);
            deviceRepository.save(device);
            return "ok";
        }
        return "error";
    }

    public String deleteAssociationToClient(DeviceDTO deviceDTO){
        Device device = deviceRepository.findByName(deviceDTO.getName()).orElse(null);
        if(device != null){
            if(device.getOwner()!=null){
                device.setOwner(null);
                deviceRepository.save(device);
            }
            log.debug("Device with id {} has no owner anymore", device.getId());
            return "ok";
        }
        return "error";
    }

    public List<Device> findDeviceByOwner(Person owner){
        return deviceRepository.findByOwner(owner);
    }

}
