package com.edu.fateczl.model.entities;

public class Bus {

    private long id;
    private String licensePlate;
    private String brand;
    private int seatsNumber;
    private boolean isEletric;
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
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }
    public void setSeatsNumber(int seatsNumber) {
        this.seatsNumber = seatsNumber;
    }
    public boolean isEletric() {
        return isEletric;
    }
    public void setEletric(boolean isEletric) {
        this.isEletric = isEletric;
    }
    public String getLine() {
        return line;
    }
    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return licensePlate + " | " + line;
    }
}
