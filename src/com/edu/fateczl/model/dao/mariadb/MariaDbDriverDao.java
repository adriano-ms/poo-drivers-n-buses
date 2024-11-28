package com.edu.fateczl.model.dao.mariadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.edu.fateczl.controller.Observer;
import com.edu.fateczl.model.dao.DbException;
import com.edu.fateczl.model.dao.IDriverDao;
import com.edu.fateczl.model.dao.NotFoundException;
import com.edu.fateczl.model.entities.Bus;
import com.edu.fateczl.model.entities.Driver;

public class MariaDbDriverDao implements IDriverDao{

    private ConcurrentHashMap<Long, Driver> cash;

    private Set<Observer> observers;

    private static MariaDbDriverDao instance;

    private MariaDbDriverDao(){
        observers = new HashSet<>();
    }

    public synchronized static MariaDbDriverDao getInstance(){
        if(instance == null) {
            instance = new MariaDbDriverDao();
            MariaDbBusDao.getInstance().subscribe(instance);
        }
        return instance;
    }

    @Override
    public void insert(Driver driver) throws DbException {
        if(cash == null) 
            loadCacheFromDatabase();

        try(Connection conn = MariaDbConnection.getConnection()) {
            String sql = "INSERT INTO drivers (driver_license, name, admission_date, shift, phone, bus_id) VALUES (? ,?, ?, ?, ? ,?);";
            PreparedStatement  statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, driver.getDriverLicense());
            statement.setString(2, driver.getName());
            statement.setString(3, driver.getAdmissionDate().toString());
            statement.setString(4, driver.getShift());
            statement.setString(5, driver.getPhone());
            statement.setLong(6, driver.getBus().getId());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.first();
            long id = rs.getLong(1);
            driver.setId(id);
            cash.put(id, driver);
            changeState();
        } catch (SQLException e) {
            throw new DbException("Erro na inserção!");
        }
    }

    @Override
    public void update(Driver driver) throws DbException {
        if(cash == null) 
            loadCacheFromDatabase();

        try(Connection conn = MariaDbConnection.getConnection()) {
            String sql = "UPDATE drivers SET driver_license = ?, name = ?, admission_date = ?, shift = ?, phone = ?, bus_id = ? WHERE id = ?;";
            PreparedStatement  statement = conn.prepareStatement(sql);
            statement.setString(1, driver.getDriverLicense());
            statement.setString(2, driver.getName());
            statement.setString(3, driver.getAdmissionDate().toString());
            statement.setString(4, driver.getShift());
            statement.setString(5, driver.getPhone());
            statement.setLong(6, driver.getBus().getId());
            statement.setLong(7, driver.getId());
            statement.executeUpdate();
            cash.replace(driver.getId(), driver);
            changeState();
        } catch (SQLException e) {
            throw new DbException("Erro na deleção!");
        }
    }

    @Override
    public void delete(long id) throws DbException {
        if(cash == null) 
            loadCacheFromDatabase();

        try (Connection conn = MariaDbConnection.getConnection()) {
            String sql = "DELETE FROM drivers WHERE id = ?;";
            PreparedStatement  statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
            cash.remove(id);
            changeState();
        } catch (SQLException e) {
            throw new DbException("Erro na deleção!");
        }
    }

    @Override
    public Driver findOne(long id) throws DbException, NotFoundException {
        if(cash == null) 
            loadCacheFromDatabase();

        Driver d = cash.get(id);
        if(d == null)
            throw new NotFoundException("Motorista não encontrado!");
        return d;
    }

    @Override
    public List<Driver> findAll() throws DbException {
        if(cash == null) 
            loadCacheFromDatabase();
            
        return new ArrayList<>(cash.values());
    }

    private void loadCacheFromDatabase() throws DbException{
        cash = new ConcurrentHashMap<>();
        try (Connection conn = MariaDbConnection.getConnection()) {
            String sql = 
                "SELECT id, driver_license, name, admission_date, shift, phone, bus_id FROM drivers;";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.first())
                do {
                    Driver driver = new Driver();
                    driver.setId(resultSet.getLong("id"));
                    driver.setDriverLicense(resultSet.getString("driver_license"));
                    driver.setName(resultSet.getString("name"));
                    driver.setAdmissionDate(LocalDate.parse(resultSet.getString("admission_date")));
                    driver.setShift(resultSet.getString("shift"));
                    driver.setPhone(resultSet.getString("phone"));
                    Bus bus = MariaDbBusDao.getInstance().findOne(resultSet.getLong("bus_id"));
                    driver.setBus(bus);
                    cash.put(driver.getId(), driver);
                }while(resultSet.next());
        } catch (SQLException e) {
            throw new DbException("Erro na consulta!");
        }
    }

    private void loadBusesFromDatabase(){
        cash.forEach((k, d) -> {
            d.setBus(MariaDbBusDao.getInstance().findOne(d.getBus().getId()));
        });
    }

    @Override
    public void subscribe(Observer o) {
        observers.add(o);
    }

    @Override
    public void unsubscribe(Observer o) {
        observers.remove(o);
    }

    @Override
    public void changeState() {
        observers.forEach(o -> o.update());
    }

    @Override
    public void update() {
        loadBusesFromDatabase();
    }
    
}
