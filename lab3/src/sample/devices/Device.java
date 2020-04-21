package sample.devices;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public abstract class Device implements Serializable {
 //   private static final long serialVersionUID = 770295594962823251L;

    private String serialNumber;
    private String manufacturer;


    public Device(){};
    
    public Device(String str){
        String[] arr = str.split("/");
        this.serialNumber = arr[0].trim();
        this.manufacturer = arr[1].trim();
    }


    public void setSerialNumber(String sn) { //protected
        this.serialNumber = sn;
    }

    public void setManufacturer(String m) {
        this.manufacturer = m;
    }

    public void getSerialNumber() {
        System.out.println("Serial number: " + this.serialNumber);
    }

    public void getManufacturer() {
        System.out.println("Manufacturer: " + this.manufacturer);

    }

    @Override
    @JsonValue
    public String toString() {
        return this.serialNumber + " / " + this.manufacturer;
    }
}