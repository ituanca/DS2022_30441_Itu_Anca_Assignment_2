package ro.tuc.messageproducer.devices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    private Integer id;

    private String name;

    private String description;

    private String address;

    private Double maxHourlyEnergyConsumption;

}
