package com.trashbase.entities;

public class Actuator {

    Integer idActuator;
    Integer idDevice;
    String state;

    public Actuator(){

    }
    
    public Actuator(Integer idSensor, Integer idDevice, String state){
        this.idActuator = idSensor;
        this.idDevice = idDevice;
        this.state = state;
    }

    public Integer getIdActuator(){
        return this.idActuator;
    }

    public Integer getIdDevice(){
        return this.idDevice;
    }

    public String getState(){
        return this.state;
    }

    public String toString(){
        String ret = "Actuator ID: " + this.idActuator + "\nDevice ID: " + this.idDevice + "\nState: " + this.state;
        return ret;
    }
}
