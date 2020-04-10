package sample.devices;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public class PC extends Device implements Serializable {
    private int ram;
    private String processor;
    private int cores_number;


    public PC(){};

    public PC(String str){
        super(str);
        String[] arr = str.split("/");
        this.ram = Integer.parseInt(arr[2].trim());
        this.processor = arr[3].trim();
        this.cores_number = Integer.parseInt(arr[4].trim());
    }


    public void setRAM(int r) {
        this.ram = r;
    }

    public void setProcessor(String p) {
        this.processor = p;
    }

    public void setCoresNumber(int n){
        this.cores_number=n;
    }

    public void getRAM() {
        System.out.println("RAM: " + this.ram);
    }

    public void getProcessor() {
        System.out.println("Processor: " + this.processor);

    }

    public void getCoresNumber(){
        System.out.println("Number of cores: " + this.cores_number);
    }

    @Override
    public String toString() {
        return super.toString() + " / " + this.ram + " / " + this.processor + " / " + this.cores_number;
    }
}