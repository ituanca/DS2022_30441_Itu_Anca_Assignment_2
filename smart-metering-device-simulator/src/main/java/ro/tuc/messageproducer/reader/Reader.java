package ro.tuc.messageproducer.reader;

import java.io.*;

public class Reader {

    Double energyConsumptionValue;

    public Double readFromFile() throws IOException{
        int lineNumber = 0;
        try(BufferedReader brSensor = new BufferedReader(new FileReader("src/main/resources/sensor-files/sensor.csv"))) {
            String readValue = brSensor.readLine();
            while(readValue != null){
                lineNumber++;
                try(BufferedReader brLastReadLine = new BufferedReader(new FileReader("src/main/resources/sensor-files/lastReadLine.txt"))) {
                    String lastReadLine = brLastReadLine.readLine();
                    if(lastReadLine == null || (lineNumber > Integer.parseInt(lastReadLine))) {
                        energyConsumptionValue = Double.parseDouble(readValue);
                        FileWriter writer = new FileWriter("src/main/resources/sensor-files/lastReadLine.txt", false); // overwrite the existent value, if it exists
                        writer.write(Integer.toString(lineNumber));
                        writer.close();
                        System.out.println(energyConsumptionValue);
                        return energyConsumptionValue;
                    }
                }
                readValue = brSensor.readLine();
            }
        }
        return null;
    }

}
