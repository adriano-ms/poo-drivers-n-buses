package com.edu.fateczl.model.dao.mariadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.edu.fateczl.controller.Observer;
import com.edu.fateczl.model.dao.DbException;
import com.edu.fateczl.model.dao.IBusDao;
import com.edu.fateczl.model.dao.NotFoundException;
import com.edu.fateczl.model.entities.Bus;

public class MariaDbBusDao implements IBusDao {

    private ConcurrentHashMap<Long, Bus> cash;

    private Set<Observer> observers;

    private static MariaDbBusDao instance;

    private MariaDbBusDao(){
        observers = new HashSet<>();
    }

    public synchronized static MariaDbBusDao getInstance(){
        if(instance == null)
            instance = new MariaDbBusDao();

        return instance;
    }

    @Override
    public void insert(Bus bus) throws DbException{
        if(cash == null) 
            loadCacheFromDatabase();

        try(Connection conn = MariaDbConnection.getConnection()){
            String sql = "INSERT INTO buses (license_plate, brand, seats_number, is_eletric, line) VALUES (? ,? , ?, ?, ?);";
            PreparedStatement  statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, bus.getLicensePlate());
            statement.setString(2, bus.getBrand());
            statement.setInt(3, bus.getSeatsNumber());
            statement.setBoolean(4, bus.isEletric());
            statement.setString(5, bus.getLine());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.first();
            long id = rs.getLong(1);
            bus.setId(id);
            cash.put(id, bus);
            changeState();
        } catch (SQLException e) {
            throw new DbException("Erro na inserção!");
        }
    }

    @Override
    public void update(Bus bus) throws DbException{
        if(cash == null) 
            loadCacheFromDatabase();

        try(Connection conn = MariaDbConnection.getConnection()) {
            String sql = "UPDATE buses SET license_plate = ?, brand = ?, seats_number = ?, is_eletric = ?, line = ? WHERE id = ?;";
            PreparedStatement  statement = conn.prepareStatement(sql);
            statement.setString(1, bus.getLicensePlate());
            statement.setString(2, bus.getBrand());
            statement.setInt(3, bus.getSeatsNumber());
            statement.setBoolean(4, bus.isEletric());
            statement.setString(5, bus.getLine());
            statement.setLong(6, bus.getId());
            statement.executeUpdate();
            cash.replace(bus.getId(), bus);
            changeState();
        } catch (SQLException e) {
            throw new DbException("Erro na atualização!");
        }
    }

    @Override
    public void delete(long id) throws DbException{
        if(cash == null) 
            loadCacheFromDatabase();

        try (Connection conn = MariaDbConnection.getConnection()) {
            String sql = "DELETE FROM buses WHERE id = ?;";
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
    public Bus findOne(long id) throws DbException, NotFoundException{
        if(cash == null)
            loadCacheFromDatabase();

        Bus b = cash.get(id);
        if(b == null)
            throw new NotFoundException("Ônibus não encontrado!");
        return b;
    }

    @Override
    public List<Bus> findAll() throws DbException{
        if(cash == null) 
            loadCacheFromDatabase();

        return new ArrayList<>(cash.values());
    }

    private void loadCacheFromDatabase() throws DbException{
        cash = new ConcurrentHashMap<>();
        try (Connection conn = MariaDbConnection.getConnection()) {
            String sql = 
                "SELECT id, license_plate, brand, seats_number, is_eletric, line FROM buses";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.first())
                do {
                    Bus bus = new Bus();
                    bus.setId(resultSet.getLong("id"));
                    bus.setLicensePlate(resultSet.getString("license_plate"));
                    bus.setBrand(resultSet.getString("brand"));
                    bus.setSeatsNumber(resultSet.getInt("seats_number"));
                    bus.setEletric(resultSet.getBoolean("is_eletric"));
                    bus.setLine(resultSet.getString("line"));
                    cash.put(bus.getId(), bus);
                }while(resultSet.next());
        } catch (SQLException e) {
            throw new DbException("Erro na consulta!");
        }
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
}
