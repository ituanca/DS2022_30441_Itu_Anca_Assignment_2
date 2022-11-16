package ro.tuc.ds2022.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2022.entities.HourlyEnergyConsumption;

import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO extends RepresentationModel<DeviceDTO> {

    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String address;

    @NotNull
    private Double maxHourlyEnergyConsumption;

    private PersonDetailsDTO owner;

    private List<HourlyEnergyConsumptionDTO> hourlyConsumption;

    public DeviceDTO(Integer id, String name, String description, String address, Double maxHourlyEnergyConsumption) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }

    public DeviceDTO(Integer id, String name, String description, String address,
                     PersonDetailsDTO owner, Double maxHourlyEnergyConsumption) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        this.owner = owner;
    }

    public DeviceDTO(Integer id, String name, String description, String address,
                     Double maxHourlyEnergyConsumption, List<HourlyEnergyConsumptionDTO> hourlyConsumption) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        this.hourlyConsumption = hourlyConsumption;
    }

}
