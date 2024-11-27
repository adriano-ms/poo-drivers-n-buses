package com.edu.fateczl.model.entities;

public class Driver {

    private long id;
    private String driverLicense;
    private String name;
    private Bus bus;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getDriverLicense() {
        return driverLicense;
    }
    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Bus getBus() {
        return bus;
    }
    public void setBus(Bus bus) {
        this.bus = bus;
    }
}
