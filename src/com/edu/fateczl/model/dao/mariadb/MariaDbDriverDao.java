package com.edu.fateczl.model.dao.mariadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.edu.fateczl.model.dao.DbException;
import com.edu.fateczl.model.dao.IDriverDao;
import com.edu.fateczl.model.dao.NotFoundException;
import com.edu.fateczl.model.entities.Bus;
import com.edu.fateczl.model.entities.Driver;

public class MariaDbDriverDao implements IDriverDao{

    private ConcurrentHashMap<Long, Driver> cash;

    private static MariaDbDriverDao instance;

    private MariaDbDriverDao(){
        super();
    }

    public synchronized static MariaDbDriverDao getInstance(){
        if(instance == null)
            instance = new MariaDbDriverDao();
        return instance;
    }

    @Override
    public void insert(Driver driver) throws DbException {
        if(cash == null) 
            loadCacheFromDatabase();

        try(Connection conn = Db.getConnection()) {
            String sql = "INSERT INTO drivers (driver_license, name, bus_id) VALUES (? ,?, ?);";
            PreparedStatement  statement = conn.prepareStatement(sql);
            statement.setString(1, driver.getDriverLicense());
            statement.setString(2, driver.getName());
            statement.setLong(3, driver.getBus().getId());
            long id = statement.executeUpdate();
            driver.setId(id);
            cash.put(id, driver);
        } catch (SQLException e) {
            throw new DbException("Erro na inserção!");
        }
    }

    @Override
    public void update(Driver driver) throws DbException {
        if(cash == null) 
            loadCacheFromDatabase();

        try(Connection conn = Db.getConnection()) {
            String sql = "UPDATE drivers SET driver_license = ?, name = ?, bus_id = ? WHERE id = ?;";
            PreparedStatement  statement = conn.prepareStatement(sql);
            statement.setString(1, driver.getDriverLicense());
            statement.setString(2, driver.getName());
            statement.setLong(3, driver.getBus().getId());
            statement.executeUpdate();
            cash.replace(driver.getId(), driver);
        } catch (SQLException e) {
            throw new DbException("Erro na atualização!");
        }
    }

    @Override
    public void delete(long id) throws DbException {
        if(cash == null) 
            loadCacheFromDatabase();

        try (Connection conn = Db.getConnection()) {
            String sql = "DELETE FROM drivers WHERE id = ?;";
            PreparedStatement  statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
            cash.remove(id);
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
        try (Connection conn = Db.getConnection()) {
            String sql = 
                "SELECT id, driver_license, name, bus_id FROM drivers;";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.first())
                do {
                    Driver driver = new Driver();
                    driver.setId(resultSet.getLong("id"));
                    driver.setDriverLicense("driver_license");
                    driver.setName(resultSet.getString("name"));
                    Bus bus = MariaDbBusDao.getInstance().findOne(resultSet.getLong("bus_id"));
                    driver.setBus(bus);
                    cash.put(driver.getId(), driver);
                }while(resultSet.next());
        } catch (SQLException e) {
            throw new DbException("Erro na consulta!");
        }
    }
    
}
