package ro.tuc.ds2022.dtos.builders;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ro.tuc.ds2022.dtos.PersonDetailsDTO;
import ro.tuc.ds2022.entities.Device;
import ro.tuc.ds2022.entities.Person;

import java.util.ArrayList;

public class PersonBuilder {

    private PersonBuilder() {
    }

    public static PersonDetailsDTO toPersonDetailsDTO(Person person) {
        PersonDetailsDTO dto = new PersonDetailsDTO(person.getId(), person.getName(), person.getUsername(),
                person.getAddress(), person.getAge(), person.getType());
        if(!person.getDevices().isEmpty()){
            dto.setOwnedDevices(new ArrayList<>());
            for(Device d: person.getDevices()){
                dto.getOwnedDevices().add(DeviceBuilder.toDeviceDTOWithoutOwnerWithoutConsumption(d));
            }
        }
        return dto;
    }

    public static Person toEntity(PersonDetailsDTO personDetailsDTO) {
        Person person = new Person(personDetailsDTO.getName(),
                personDetailsDTO.getUsername(),
                personDetailsDTO.getAddress(),
                personDetailsDTO.getAge(),
                personDetailsDTO.getType());
        if(personDetailsDTO.getPassword() != null){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            person.setPassword(bCryptPasswordEncoder.encode(personDetailsDTO.getPassword()));
        }
        return person;
    }
}
