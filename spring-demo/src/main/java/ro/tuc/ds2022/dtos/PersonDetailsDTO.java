package ro.tuc.ds2022.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2022.dtos.validators.annotation.AgeLimit;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDetailsDTO extends RepresentationModel<PersonDetailsDTO> {

    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String address;

    @AgeLimit(limit = 18)
    private int age;

    @NotNull
    private String type;

    private List<DeviceDTO> ownedDevices;

    public PersonDetailsDTO( String name, String username, String password, String address, int age, String type) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.age = age;
        this.type = type;
    }

    public PersonDetailsDTO( String name, String username, String address, int age, String type) {
        this.name = name;
        this.username = username;
        this.address = address;
        this.age = age;
        this.type = type;
    }

    public PersonDetailsDTO( Integer id, String name, String username, String address, int age, String type) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.address = address;
        this.age = age;
        this.type = type;
    }

    public PersonDetailsDTO( Integer id, String name, String username, String address,
                             int age, String type, List<DeviceDTO> ownedDevices) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.address = address;
        this.age = age;
        this.type = type;
        this.ownedDevices = ownedDevices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDetailsDTO that = (PersonDetailsDTO) o;
        return age == that.age &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, age);
    }
}
