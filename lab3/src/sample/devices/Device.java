package sample.devices;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public abstract class Device implements Serializable {
    private String serial_number;
    private String manufacturer;


    public Device(){};

    public Device(String str){
        String[] arr = str.split("/");
        this.serial_number = arr[0].trim();
        this.manufacturer = arr[1].trim();
    }


    public void setSerialNumber(String sn) { //protected
        this.serial_number = sn;
    }

    public void setManufacturer(String m) {
        this.manufacturer = m;
    }

    public void getSerialNumber() {
        System.out.println("Serial number: " + this.serial_number);
    }

    public void getManufacturer() {
        System.out.println("Manufacturer: " + this.manufacturer);

    }

    @Override
    @JsonValue
    public String toString() {
        return this.serial_number + " / " + this.manufacturer;
    }
}