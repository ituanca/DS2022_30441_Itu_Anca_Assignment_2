package ro.tuc.ds2022.wrappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ro.tuc.ds2022.entities.Device;

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
