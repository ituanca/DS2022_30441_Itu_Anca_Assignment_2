package ro.tuc.messageproducer.measurements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {

    private LocalDateTime timestamp;

    private Integer deviceId;

    private Double energyConsumption;

}
