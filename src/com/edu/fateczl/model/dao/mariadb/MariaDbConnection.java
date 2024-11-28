package com.edu.fateczl.model.dao.mariadb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.edu.fateczl.model.dao.DbException;

public class MariaDbConnection {

    private MariaDbConnection(){
        super();
    }

    private static Connection conn;
    private static Properties properties;

    public static Connection getConnection() throws DbException{
        try {
            if(conn == null){
                loadProperties();
                String url = properties.getProperty("url");
                conn = DriverManager.getConnection(url, properties);
            }
            if(conn.isClosed()){
                String url = properties.getProperty("url");
                conn = DriverManager.getConnection(url, properties);
            }
            return conn;
        } catch (SQLException e) {
            throw new DbException("Erro ao conectar-se ao banco de dados!");
        }
    }

    private static void loadProperties() throws DbException{
        try (BufferedReader reader = new BufferedReader(new FileReader("database.properties"))) {
            properties = new Properties();
            properties.load(reader);
        } catch (IOException e) {
            throw new DbException("Erro ao carregar as configurações do banco de dados!");
        }
    }
}
