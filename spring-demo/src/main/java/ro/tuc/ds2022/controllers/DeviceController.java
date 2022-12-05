package ro.tuc.ds2022.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2022.dtos.DeviceDTO;
import ro.tuc.ds2022.entities.Device;
import ro.tuc.ds2022.wrappers.DeviceList;
import ro.tuc.ds2022.services.DeviceService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findListDeviceDTO();
        for(DeviceDTO dto : dtos){
            Link deviceLink = linkTo(methodOn(DeviceController.class).getDevice(dto.getId())).withRel("devices");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") Integer deviceId) {
        DeviceDTO dto = deviceService.findDeviceDTOById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> insertDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        System.out.println(deviceDTO);
        String response = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<String> updateDevice(@RequestBody DeviceDTO deviceDTO) {
        String response = deviceService.update(deviceDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{name}")
    public ResponseEntity<String> deleteDevice(@PathVariable("name") String name) {
        String response = deviceService.delete(name);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/withoutOwner")
    public ResponseEntity<List<DeviceDTO>> getDevicesWithoutOwner() {
        List<DeviceDTO> dtos = deviceService.findDevicesDTOWithoutOwner();
        for(DeviceDTO dto : dtos){
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("devices");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping(value = "/addOwner")
    public ResponseEntity<String> addOwner(@RequestBody List<String> data) {
        String response = deviceService.createAssociation(data.get(0), data.get(1));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/withOwner")
    public ResponseEntity<List<DeviceDTO>> getDevicesDTOWithOwner() {
        List<DeviceDTO> dtos = deviceService.findDevicesDTOWithOwner();
        for(DeviceDTO dto : dtos){
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("devices");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/devicesWithOwner")
    public DeviceList getDevicesWithOwner() {
        return deviceService.findDeviceListWithOwner();
    }

    @PutMapping(value = "/updateAssociation")
    public ResponseEntity<String> updateAssociation(@RequestBody List<String> data) {
        String response = deviceService.updateAssociation(data.get(0), data.get(1));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/deleteAssociation")
    public ResponseEntity<String> deleteAssociation(@RequestBody DeviceDTO deviceDTO) {
        String response = deviceService.deleteAssociationToClient(deviceDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/deviceById/{id}")
    public Device getDeviceById(@PathVariable("id") Integer deviceId) {
        return deviceService.findDeviceById(deviceId);
    }

}
