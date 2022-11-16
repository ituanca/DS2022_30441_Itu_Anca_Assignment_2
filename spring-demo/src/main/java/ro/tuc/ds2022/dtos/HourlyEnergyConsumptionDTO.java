package ro.tuc.ds2022.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.tuc.ds2022.entities.Device;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HourlyEnergyConsumptionDTO {

    private Integer id;

    private DeviceDTO device;

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    private Double energyConsumption;

    public HourlyEnergyConsumptionDTO(Integer id, LocalDateTime timestamp, Double energyConsumption) {
        this.id = id;
        this.timestamp = timestamp;
        this.energyConsumption = energyConsumption;
    }
}
