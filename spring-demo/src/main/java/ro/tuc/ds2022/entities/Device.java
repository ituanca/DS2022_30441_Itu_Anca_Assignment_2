package ro.tuc.ds2022.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "maxHourlyEnergyConsumption", nullable = false)
    private Double maxHourlyEnergyConsumption;

    @ManyToOne
    @JoinColumn(name = "idOwner")
    private Person owner;

    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<HourlyEnergyConsumption> listHourlyConsumption;

    public Device(Integer id, String name, String description, String address, Double maxHourlyEnergyConsumption, Person owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        this.owner = owner;
    }

    public Device(String name, String description, String address, Double maxHourlyEnergyConsumption) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }
}
