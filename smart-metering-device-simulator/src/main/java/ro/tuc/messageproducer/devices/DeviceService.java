package ro.tuc.messageproducer.devices;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class DeviceService {

    public List<Device> getDevices() {
        String URL = "http://localhost:8080/device/devicesWithOwner";
        DeviceList response = new RestTemplate().getForObject(URL, DeviceList.class);
        List<Device> devices =  new ArrayList<>();
        if(response!=null){
            devices = response.getDevices();
        }
        for(Device d : devices){
            System.out.println(d.getId() + " " + d.getName() + " " + d.getAddress());
        }
        return devices;
    }

}
