package sample.devices;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public class Accumulator implements Serializable {
  //  private static final long serialVersionUID = -7550665257233735339L;

    private int maxWorkTime;

    public Accumulator(){};

    public Accumulator(String str){
        this.maxWorkTime = Integer.parseInt(str);
    }

    public void setMaxWorkTime(int t){
        this.maxWorkTime=t;
    }

    public void getMaxWorkTime(){
        System.out.println("Max working time without recharging: " + this.maxWorkTime+"h");
    }

    @Override
    @JsonValue
    public String toString() {
        return this.maxWorkTime + "";
    }
}
