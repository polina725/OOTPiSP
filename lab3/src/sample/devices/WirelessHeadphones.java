package sample.devices;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public class WirelessHeadphones extends Device implements Serializable {
    private String type;
    private String case_material;
    private Accumulator battery;



    public WirelessHeadphones(){};

    public WirelessHeadphones(String str){
        super(str);
        String[] arr = str.split("/");
        this.type = arr[2].trim();
        this.case_material = arr[3].trim();
        this.battery = new Accumulator(arr[4].trim());
    }



    public void setType(String tp){
        this.type=tp;
    }

    public void setCaseMaterial(String cm){
        this.case_material=cm;
    }

    public void setBattery(Accumulator bt){
        this.battery=bt;
    }

    public void getType(){
        System.out.println("Type: " + this.type);
    }

    public void getCaseMaterial(){
        System.out.println("Case material: " + this.case_material);
    }

    public void getBattery(){
        this.battery.getMaxWorkTime();
    }

    @Override
    @JsonValue
    public String toString() {
        return super.toString() + " / " + this.type + " / " + this.case_material + " / " + battery.toString();
    }
}
