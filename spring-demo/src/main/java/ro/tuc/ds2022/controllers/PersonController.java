package ro.tuc.ds2022.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2022.dtos.DeviceDTO;
import ro.tuc.ds2022.dtos.HourlyEnergyConsumptionDTO;
import ro.tuc.ds2022.dtos.PersonDetailsDTO;
import ro.tuc.ds2022.services.PersonService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {

    @Autowired
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public ResponseEntity<List<PersonDetailsDTO>> getPersons() {
        List<PersonDetailsDTO> dtos = personService.findPersons();
        for (PersonDetailsDTO dto : dtos) {
            Link personLink = linkTo(methodOn(PersonController.class)
                    .getPerson(dto.getId())).withRel("personDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDetailsDTO> getPerson(@PathVariable("id") Integer personId) {
        PersonDetailsDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/clients")
    public ResponseEntity<List<PersonDetailsDTO>> getClients() {
        List<PersonDetailsDTO> dtos = personService.findClients();
        for (PersonDetailsDTO dto : dtos) {
            Link personLink = linkTo(methodOn(PersonController.class)
                    .getPerson(dto.getId())).withRel("personDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<PersonDetailsDTO> getPersonByUsername(@PathVariable("username") String username) {
        PersonDetailsDTO dto = personService.findPersonDetailsDTOByUsername(username);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> insertProsumer(@Valid @RequestBody PersonDetailsDTO personDTO) {
        String response = personService.insert(personDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //TODO: UPDATE, DELETE per resource

    @GetMapping("/login")
    public String login(@Param("username") String username, @Param("password") String password){
        return personService.login(username, password);
    }

    @PutMapping()
    public ResponseEntity<String> updateProsumer(@RequestBody PersonDetailsDTO personDTO) {
        String response = personService.update(personDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{username}")
    public ResponseEntity<String> deleteProsumer(@PathVariable("username") String username) {
        String response = personService.delete(username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/clientsWithDevices")
    public ResponseEntity<List<PersonDetailsDTO>> getClientsWithDevices() {
        List<PersonDetailsDTO> dtos = personService.findClientsWithDevices();
        for (PersonDetailsDTO dto : dtos) {
            Link personLink = linkTo(methodOn(PersonController.class)
                    .getPerson(dto.getId())).withRel("personDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/ownedDevicesWithoutHourlyConsumption")
    public ResponseEntity<List<DeviceDTO>> getOwnedDevicesWithoutConsumption(@Param("username") String username) {
        List<DeviceDTO> dtos = personService.findOwnedDevicesWithoutConsumption(username);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/ownedDevicesWithHourlyConsumption")
    public ResponseEntity<List<DeviceDTO>> getOwnedDevicesWithConsumption(@Param("username") String username) {
        List<DeviceDTO> dtos = personService.findOwnedDevicesWithConsumption(username);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}
