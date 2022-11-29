package ro.tuc.ds2022.measurements;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Measurement implements Serializable{

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("deviceId")
    private Integer deviceId;

    @JsonProperty("energyConsumption")
    private Double energyConsumption;

    @Override
    public String toString() {
        return timestamp +
                ", " + deviceId +
                ", " + energyConsumption;
    }
}
