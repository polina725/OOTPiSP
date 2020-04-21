package sample.devices;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public class WirelessHeadphones extends Device implements Serializable {
 //   private static final long serialVersionUID = 2420007189669215843L;

    private String type;
    private String caseMaterial;
    private Accumulator battery;

    public WirelessHeadphones(){};

    public WirelessHeadphones(String str){
        super(str);
        String[] arr = str.split("/");
        this.type = arr[2].trim();
        this.caseMaterial = arr[3].trim();
        this.battery = new Accumulator(arr[4].trim());
    }



    public void setType(String tp){
        this.type=tp;
    }

    public void setCaseMaterial(String cm){
        this.caseMaterial=cm;
    }

    public void setBattery(Accumulator bt){
        this.battery=bt;
    }

    public void getType(){
        System.out.println("Type: " + this.type);
    }

    public void getCaseMaterial(){
        System.out.println("Case material: " + this.caseMaterial);
    }

    public void getBattery(){
        this.battery.getMaxWorkTime();
    }

    @Override
    @JsonValue
    public String toString() {
        return super.toString() + " / " + this.type + " / " + this.caseMaterial + " / " + battery.toString();
    }
}
