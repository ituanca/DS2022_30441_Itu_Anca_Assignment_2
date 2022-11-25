package ro.tuc.messageproducer.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

    Double energyConsumptionValue;

    public Double readFromFile() throws IOException{
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/sensor-files/sensor.csv"))) {
            String readValue;
            if((readValue = br.readLine()) != null){
                energyConsumptionValue = Double.parseDouble(readValue);
            }
        }
        System.out.println(energyConsumptionValue);
        return energyConsumptionValue;
    }
}
