package com.edu.fateczl.controller;

import java.time.LocalDate;
import com.edu.fateczl.model.dao.DbException;
import com.edu.fateczl.model.dao.IBusDao;
import com.edu.fateczl.model.dao.IDriverDao;
import com.edu.fateczl.model.dao.NotFoundException;
import com.edu.fateczl.model.dao.mariadb.MariaDbBusDao;
import com.edu.fateczl.model.dao.mariadb.MariaDbDriverDao;
import com.edu.fateczl.model.entities.Bus;
import com.edu.fateczl.model.entities.Driver;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DriverController implements Observer{

    private ObservableList<Driver> drivers;
    private LongProperty idProperty;
    private StringProperty licenseProperty;
    private StringProperty nameProperty;
    private ObjectProperty<LocalDate> admissionDateProperty;
    private StringProperty shiftProperty;
    private StringProperty phoneProperty;
    private ObjectProperty<Bus> busProperty;
    private ObservableList<Bus> buses;

    private StringProperty searchProperty;

    private IDriverDao driverDao;
    private IBusDao busDao;

    public DriverController(){
        drivers = FXCollections.observableArrayList();
        idProperty = new SimpleLongProperty();
        licenseProperty = new SimpleStringProperty();
        nameProperty = new SimpleStringProperty();
        admissionDateProperty = new SimpleObjectProperty<>();
        shiftProperty = new SimpleStringProperty();
        phoneProperty = new SimpleStringProperty();
        busProperty = new SimpleObjectProperty<>();
        searchProperty = new SimpleStringProperty();
        buses = FXCollections.observableArrayList();
        driverDao = MariaDbDriverDao.getInstance();
        busDao = MariaDbBusDao.getInstance();
        driverDao.subscribe(this);
        busDao.subscribe(this);
    }

    public void insertDriver() throws DbException, InvalidInputException{
        String license = licenseProperty.getValue();
        String name = nameProperty.getValue();
        LocalDate admissionDate = admissionDateProperty.getValue();
        String shift = shiftProperty.getValue();
        String phone = phoneProperty.getValue();
        if(license == null || license.isBlank())
            throw new InvalidInputException("CNH inválida!");
        if(name == null || license.isBlank())
            throw new InvalidInputException("Nome inválido!");
        if(admissionDate == null)
            throw new InvalidInputException("Admissão inválida!");
        if(shift == null || shift.isBlank())
            throw new InvalidInputException("Turno inválido!");
        if(phone == null || phone.isBlank())
            throw new InvalidInputException("Telefone inválido!");
        if(busProperty.getValue() == null)
            throw new InvalidInputException("Ônibus inválido!");
        Driver driver = new Driver();
        driver.setDriverLicense(license);
        driver.setName(name);
        driver.setAdmissionDate(admissionDate);
        driver.setShift(shift);
        driver.setPhone(phone);
        driver.setBus(busProperty.getValue());
        if(idProperty.getValue() == 0)
            driverDao.insert(driver);
        else {
            driver.setId(idProperty.get());
            driverDao.update(driver);
        }
        clearFields();
    }

    public void deleteDriver(long id) throws DbException{
        driverDao.delete(id);
        if(idProperty.getValue() == 0)
            clearFields();
    }

    public void entityToBoundary(Driver driver){
        idProperty.setValue(driver.getId());
        licenseProperty.setValue(driver.getDriverLicense());
        nameProperty.setValue(driver.getName());
        admissionDateProperty.setValue(driver.getAdmissionDate());
        shiftProperty.setValue(driver.getShift());
        phoneProperty.setValue(driver.getPhone());
        busProperty.setValue(driver.getBus());
    }

    public void clearFields(){
        idProperty.setValue(0);
        licenseProperty.setValue("");
        nameProperty.setValue("");
        admissionDateProperty.setValue(LocalDate.now());
        shiftProperty.setValue("");
        phoneProperty.setValue("");
        if(!buses.isEmpty())
            busProperty.setValue(buses.get(0));
    }

    public void findAllDrivers() throws DbException{
        drivers.setAll(driverDao.findAll());
    }

    public void findAllBuses() throws DbException{
        buses.setAll(busDao.findAll());
    }

    public void searchDrivers()  throws DbException{
        String search = searchProperty.getValue();
        findAllDrivers();
        if(search != null && !search.isBlank()) {
            String str = search.trim().toLowerCase();
            drivers.setAll(drivers.stream().filter(d -> {
                if(d.getDriverLicense().toLowerCase().contains(str))
                    return true;
                if(d.getName().toLowerCase().contains(str))
                    return true;
                if(d.getPhone().toLowerCase().contains(str))
                    return true;
                if(d.getShift().toLowerCase().contains(str))
                    return true;
                if(d.getBus().getLine().toLowerCase().contains(str))
                    return true;
                return false;
            }).toList());
        }
    }

    public ObservableList<Driver> getDriversObservableList() {
        return drivers;
    }

    public LongProperty getIdProperty() {
        return idProperty;
    }

    public StringProperty getLicenseProperty() {
        return licenseProperty;
    }

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public ObjectProperty<LocalDate> getAdmissionDateProperty() {
        return admissionDateProperty;
    }

    public StringProperty getShiftProperty() {
        return shiftProperty;
    }

    public StringProperty getPhoneProperty() {
        return phoneProperty;
    }

    public ObjectProperty<Bus> getBusProperty() {
        return busProperty;
    }

    public ObservableList<Bus> getBusesObservableList() {
        return buses;
    }

    public StringProperty getSearchProperty() {
        return searchProperty;
    }

    @Override
    public void update() {
        Bus selectedBus = null;
        if(idProperty.getValue() != 0) {
            try {
                driverDao.findOne(idProperty.longValue());
                selectedBus = busProperty.getValue();
            } catch (NotFoundException e) {
                clearFields();
            }
        }
        findAllBuses();
        findAllDrivers();
        if(selectedBus != null)
            busProperty.setValue(selectedBus);
        else if(!buses.isEmpty())
            busProperty.setValue(buses.get(0));
    }
    
}
