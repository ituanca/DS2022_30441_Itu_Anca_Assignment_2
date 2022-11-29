package ro.tuc.ds2022.entities;

import lombok.*;
import ro.tuc.ds2022.dtos.DeviceDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HourlyEnergyConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idDevice")
    private Device device;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "energyConsumption", nullable = false)
    private Double energyConsumption;

    public HourlyEnergyConsumption(Device device, LocalDateTime timestamp, Double energyConsumption) {
        this.device = device;
        this.timestamp = timestamp;
        this.energyConsumption = energyConsumption;
    }

    public HourlyEnergyConsumption(Integer id, LocalDateTime timestamp, Double energyConsumption) {
        this.id = id;
        this.timestamp = timestamp;
        this.energyConsumption = energyConsumption;
    }

}
