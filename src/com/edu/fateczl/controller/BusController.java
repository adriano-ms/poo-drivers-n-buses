package com.edu.fateczl.controller;

import com.edu.fateczl.model.dao.DbException;
import com.edu.fateczl.model.dao.IBusDao;
import com.edu.fateczl.model.dao.NotFoundException;
import com.edu.fateczl.model.dao.mariadb.MariaDbBusDao;
import com.edu.fateczl.model.entities.Bus;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BusController implements Observer{

    private ObservableList<Bus> buses;
    private LongProperty idProperty;
    private StringProperty licensePlateProperty;
    private StringProperty brandProperty;
    private StringProperty seatsNumberProperty;
    private BooleanProperty electricProperty;
    private StringProperty lineProperty;

    private StringProperty searchProperty;
    
    private IBusDao busDao;

    public BusController(){
        buses = FXCollections.observableArrayList();
        idProperty = new SimpleLongProperty();
        licensePlateProperty = new SimpleStringProperty();
        brandProperty = new SimpleStringProperty();
        seatsNumberProperty = new SimpleStringProperty();
        electricProperty = new SimpleBooleanProperty();
        lineProperty = new SimpleStringProperty();
        searchProperty = new SimpleStringProperty();
        busDao = MariaDbBusDao.getInstance();
        busDao.subscribe(this);
    }

    public void insertBus() throws DbException, InvalidInputException{
        String licensePlate = licensePlateProperty.getValue();
        String brand = brandProperty.getValue();
        String line = lineProperty.getValue();
        int seatsNumber;
        if(licensePlate == null || licensePlate.isBlank())
            throw new InvalidInputException("Placa inválida!");
        if(brand == null || brand.isBlank())
            throw new InvalidInputException("Marca inválida!");
        if(line == null || line.isBlank())
            throw new InvalidInputException("Linha inválida!");
        try {
            seatsNumber = Integer.parseInt(seatsNumberProperty.getValue());
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Assentos inválidos!");
        }
        Bus bus = new Bus();
        bus.setLicensePlate(licensePlate.trim());
        bus.setBrand(brand.trim());
        bus.setLine(line.trim());
        bus.setSeatsNumber(seatsNumber);
        bus.setEletric(electricProperty.getValue());
        if(idProperty.getValue() == 0)
            busDao.insert(bus);
        else {
            bus.setId(idProperty.getValue());
            busDao.update(bus);
        }
        clearFields();
    }

    public void deleteBus(long id) throws DbException{
        busDao.delete(id);
        if(idProperty.get() == id)
            clearFields();
    }

    public void findAllBuses() throws DbException{
        buses.setAll(busDao.findAll());
    }

    public void searchBuses()  throws DbException{
        String search = searchProperty.getValue();
        findAllBuses();
        if(search != null && !search.isBlank()) {
            String str = search.trim().toLowerCase();
            buses.setAll(buses.stream().filter(b -> {
                if(b.getBrand().toLowerCase().contains(str))
                    return true;
                if(b.getLicensePlate().toLowerCase().contains(str))
                    return true;
                if(b.getLine().toLowerCase().contains(str))
                    return true;
                return false;
            }).toList());
        }
    }

    public void entityToBoundary(Bus bus){
        idProperty.setValue(bus.getId());
        licensePlateProperty.setValue(bus.getLicensePlate());
        brandProperty.setValue(bus.getBrand());
        seatsNumberProperty.setValue(String.valueOf(bus.getSeatsNumber()));
        electricProperty.setValue(bus.isEletric());
        lineProperty.setValue(bus.getLine());
    }

    public void clearFields(){
        idProperty.setValue(0);
        licensePlateProperty.setValue("");
        brandProperty.setValue("");
        seatsNumberProperty.setValue("");
        electricProperty.setValue(false);
        lineProperty.setValue("");
    }

    public LongProperty getIdProperty(){
        return idProperty;
    }

    public ObservableList<Bus> getBusesObservableList(){
        return buses;
    }

    public StringProperty getLicensePlateProperty() {
        return licensePlateProperty;
    }

    public StringProperty getBrandProperty() {
        return brandProperty;
    }

    public StringProperty getSeatsNumberProperty() {
        return seatsNumberProperty;
    }

    public BooleanProperty getElectricProperty() {
        return electricProperty;
    }

    public StringProperty getLineProperty() {
        return lineProperty;
    }

    public StringProperty getSearchProperty(){
        return searchProperty;
    }

    @Override
    public void update() {
        if(idProperty.getValue() != 0)
        try {
            busDao.findOne(idProperty.getValue());
        } catch (NotFoundException e) {
            clearFields();
        }
        findAllBuses();
    }
    
}
