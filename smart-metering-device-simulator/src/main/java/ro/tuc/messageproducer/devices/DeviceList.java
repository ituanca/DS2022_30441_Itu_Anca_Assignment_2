package ro.tuc.messageproducer.devices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DeviceList {

    private List<Device> devices;

    public DeviceList() {
        devices = new ArrayList<>();
    }
}
