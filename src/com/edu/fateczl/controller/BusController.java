package com.edu.fateczl.controller;

import com.edu.fateczl.model.dao.DbException;
import com.edu.fateczl.model.dao.IBusDao;
import com.edu.fateczl.model.dao.mariadb.MariaDbBusDao;
import com.edu.fateczl.model.entities.Bus;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BusController {

    private ObservableList<Bus> buses;
    private StringProperty licensePlateProperty;
    private StringProperty modelProperty;
    private StringProperty lineProperty;
    
    private IBusDao busDao;

    public BusController(){
        buses = FXCollections.observableArrayList();
        licensePlateProperty = new SimpleStringProperty();
        modelProperty = new SimpleStringProperty();
        lineProperty = new SimpleStringProperty();
        busDao = MariaDbBusDao.getInstance();
    }

    public void insertBus() throws DbException, InvalidInputException{
        String licensePlate = licensePlateProperty.getValue();
        String model = modelProperty.getValue();
        String line = lineProperty.getValue();
        if(licensePlate.isBlank() || licensePlate.length() > 7)
            throw new InvalidInputException("Placa inválida!");
        if(model.isBlank())
            throw new InvalidInputException("Modelo inválido!");
        if(line.isBlank())
            throw new InvalidInputException("Linha inválida!");
        Bus bus = new Bus();
        bus.setLicensePlate(licensePlate);
        bus.setModel(model);
        bus.setLine(line);
        busDao.insert(bus);
        findAllBuses();
    }

    public void findAllBuses() throws DbException{
        buses.clear();
        buses.addAll(busDao.findAll());
    }

    public ObservableList<Bus> getBusesObservableList(){
        return buses;
    }

    public StringProperty getLicensePlateProperty() {
        return licensePlateProperty;
    }


    public StringProperty getModelProperty() {
        return modelProperty;
    }

    public StringProperty getLineProperty() {
        return lineProperty;
    }
    
}
