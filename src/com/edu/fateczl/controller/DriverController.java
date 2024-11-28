package com.edu.fateczl.controller;

import java.util.List;

import com.edu.fateczl.model.dao.IDriverDao;
import com.edu.fateczl.model.dao.mariadb.MariaDbDriverDao;
import com.edu.fateczl.model.entities.Driver;

public class DriverController {

    private IDriverDao driverDao;

    public DriverController(){;
        driverDao = MariaDbDriverDao.getInstance();
    }

    public List<Driver> listAll(){
        return driverDao.findAll();
    }

    public void createDriver(Driver driver){
        driverDao.insert(driver);
    }
    
}
