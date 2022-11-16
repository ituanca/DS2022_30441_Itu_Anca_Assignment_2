package ro.tuc.ds2022.entities;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Person implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Device> devices;

    public Person(String name, String username, String password, String address, int age, String type) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.age = age;
        this.type = type;
    }

    public Person(Integer id, String name, String username, String address, int age, String type) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.age = age;
        this.type = type;
    }

    public Person(String name, String username, String address, int age, String type) {
        this.name = name;
        this.username = username;
        this.address = address;
        this.age = age;
        this.type = type;
    }

}
