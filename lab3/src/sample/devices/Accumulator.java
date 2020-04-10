package sample.devices;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public class Accumulator implements Serializable {

    private int max_work_time;

    public Accumulator(){};

    public Accumulator(String str){
        this.max_work_time = Integer.parseInt(str);
    }

    public void setMaxWorkTime(int t){
        this.max_work_time=t;
    }

    public void getMaxWorkTime(){
        System.out.println("Max working time without recharging: " + this.max_work_time+"h");
    }

    @Override
    @JsonValue
    public String toString() {
        return this.max_work_time + "";
    }
}
