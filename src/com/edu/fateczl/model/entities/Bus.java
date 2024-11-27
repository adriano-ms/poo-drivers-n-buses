package com.edu.fateczl.model.entities;

public class Bus {

    private long id;
    private String licensePlate;
    private String model;
    private String line;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getLine() {
        return line;
    }
    public void setLine(String line) {
        this.line = line;
    }
}
