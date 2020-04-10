package sample.devices;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public class Laptop extends PC implements Serializable {
    private int weight;
    private Accumulator battery;


    public Laptop(){};

    public Laptop(String str){
        super(str);
        String[] arr = str.split("/");
        this.weight = Integer.parseInt(arr[5].trim());
        this.battery= new Accumulator(arr[6].trim());
    }


    public void setBattery(Accumulator bt){
        this.battery=bt;
    }

    public void setWeight(int w){
        this.weight=w;
    }

    public void getWeight(){
        System.out.println("Weight: "+ this.weight);
    }

    public void getBattery(){
        this.battery.getMaxWorkTime();
    }

    @Override
    @JsonValue
    public String toString() {
        return super.toString() + " / " + this.weight + " / " + this.battery.toString();
    }
}