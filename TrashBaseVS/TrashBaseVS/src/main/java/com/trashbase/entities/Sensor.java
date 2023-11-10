package com.trashbase.entities;


public class Sensor {

    Integer idSensor;
    Integer idDevice;
    String state;

    public Sensor(){

    }
    
    public Sensor(Integer idSensor, Integer idDevice, String state){
        this.idSensor = idSensor;
        this.idDevice = idDevice;
        this.state = state;
    }

    public Integer getIdSensor(){
        return this.idSensor;
    }

    public Integer getIdDevice(){
        return this.idDevice;
    }

    public String getState(){
        return this.state;
    }

    public String toString(){
        String ret = "Sensor ID: " + this.idSensor + "\nDevice ID: " + this.idDevice + "\nState: " + this.state;
        return ret;
    }

}
