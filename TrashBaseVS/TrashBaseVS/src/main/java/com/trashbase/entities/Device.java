package com.trashbase.entities;

public class Device {

    Integer idDevice;
    String name;
    
    public Device() {
		super();
	}

	public Device(Integer idDevice, String name) {
		super();
		this.idDevice = idDevice;
		this.name = name;
	}

	public int getIdDevice(){
		return idDevice;
	}

	public String getName(){
		return name;
	}

	public String toString(){
        String ret = "Device ID: " + this.idDevice + "\nName: " + this.name;
        return ret;
    }

}
