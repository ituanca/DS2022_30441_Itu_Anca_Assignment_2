package ro.tuc.ds2022.services;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.tuc.ds2022.dtos.DeviceDTO;
import ro.tuc.ds2022.dtos.builders.DeviceBuilder;
import ro.tuc.ds2022.entities.Device;
import ro.tuc.ds2022.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2022.dtos.PersonDetailsDTO;
import ro.tuc.ds2022.dtos.builders.PersonBuilder;
import ro.tuc.ds2022.entities.Person;
import ro.tuc.ds2022.repositories.PersonRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDetailsDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDetailsDTO)
                .collect(Collectors.toList());
    }

    public PersonDetailsDTO findPersonById(Integer id) {
        Optional<Person> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            log.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }

    public List<PersonDetailsDTO> findClients() {
        List<Person> personList = personRepository.findAll();
        List<Person> clients = new ArrayList<>();
        for(Person person : personList){
            if(person.getType().equals("client")){
                clients.add(person);
            }
        }
        return clients.stream()
                .map(PersonBuilder::toPersonDetailsDTO)
                .collect(Collectors.toList());
    }

    public PersonDetailsDTO findPersonDetailsDTOByUsername(String username) {
        Optional<Person> prosumerOptional = personRepository.findByUsername(username);
        if (!prosumerOptional.isPresent()) {
            log.error("Person with username {} was not found in db", username);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with username: " + username);
        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }

    public Optional<Person> findPersonByUsername(String username) {
        Optional<Person> prosumerOptional = personRepository.findByUsername(username);
        if (!prosumerOptional.isPresent()) {
            log.error("Person with username {} was not found in db", username);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with username: " + username);
        }
        return prosumerOptional;
    }

    public boolean checkIfUsernameExists(String username) {
        Optional<Person> prosumerOptional = personRepository.findByUsername(username);
        if (!prosumerOptional.isPresent()) {
            log.info("Username {} was not found in db", username);
        }
        return prosumerOptional.isPresent();
    }

    public String insert(PersonDetailsDTO personDTO) {
        if(checkIfUsernameExists(personDTO.getUsername())){
            return "username_error";
        }
        Person person = PersonBuilder.toEntity(personDTO);
        person = personRepository.save(person);
        log.debug("Person with id {} was inserted in db", person.getId());
        return "ok";
    }

    public String update(PersonDetailsDTO personDTO) {
        Person person = personRepository.findById(personDTO.getId()).orElse(null);
        if(person!=null){
            if(!person.getUsername().equals(personDTO.getUsername())){ // if username was updated
                if(checkIfUsernameExists(personDTO.getUsername())){
                    return "username_error";
                }
            }
            person.setName(personDTO.getName());
            person.setUsername(personDTO.getUsername());
            person.setAddress(personDTO.getAddress());
            person.setAge(personDTO.getAge());
            person.setType(personDTO.getType());

            personRepository.save(person);
            log.debug("Person with id {} was updated in db", person.getId());
            return "ok";
        }
        return "error";
    }

    public String delete(String username) {
        Person person = personRepository.findByUsername(username).orElse(null);
        if(person!=null){
            person.getDevices().forEach(device -> device.setOwner(null));
            personRepository.delete(person);
            log.debug("Person with id {} was deleted from db", person.getId());
            return "ok";
        }
        return "error";
    }

    public String login(String username, String password){
        Person person = personRepository.findByUsername(username).orElse(null);
        if (person == null) {
            log.warn(" Username " + username + " does not exist!");
            return "username_error";
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bCryptPasswordEncoder.matches(password, person.getPassword())){
            log.warn(" Password is incorrect!");
            return "password_error";
        }
        log.info("Credentials are valid");
        if(Objects.equals(person.getType(), "admin")){
            return "admin";
        }else if(Objects.equals(person.getType(), "client")){
            return "client";
        }
        log.info("User " + person.getType() + " has successfully logged in.");
        return "error";
    }

    public List<PersonDetailsDTO> findClientsWithDevices() {
        List<Person> personList = personRepository.findAll();
        List<Person> clientsWithDevices = new ArrayList<>();
        for(Person person : personList){
            if(person.getType().equals("client") && !person.getDevices().isEmpty()){
                clientsWithDevices.add(person);
            }
        }
        return clientsWithDevices.stream()
                .map(PersonBuilder::toPersonDetailsDTO)
                .collect(Collectors.toList());
    }

    public List<DeviceDTO> findOwnedDevicesWithoutConsumption(String username) {
        Person person = personRepository.findByUsername(username).orElse(null);
        List<Device> ownedDevices = new ArrayList<>();
        if(person!=null && !person.getDevices().isEmpty()){
            ownedDevices = person.getDevices();
        }
        return ownedDevices.stream()
                .map(DeviceBuilder::toDeviceDTOWithoutConsumption)
                .collect(Collectors.toList());
    }

    public List<DeviceDTO> findOwnedDevicesWithConsumption(String username) {
        Person person = personRepository.findByUsername(username).orElse(null);
        List<Device> ownedDevices = new ArrayList<>();
        if(person!=null && !person.getDevices().isEmpty()){
            ownedDevices = person.getDevices();
        }
        return ownedDevices.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    @PostConstruct
    private void createAdmin(){
        if(personRepository.findAll().isEmpty()){
            Person admin = new Person("Anca Itu", "ancaitu", "str.abc, nr.1", 22, "admin");
            admin.setPassword(new BCryptPasswordEncoder().encode("pass"));
            personRepository.save(admin);
        }
    }

}
