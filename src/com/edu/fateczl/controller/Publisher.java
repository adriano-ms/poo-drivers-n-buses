package com.edu.fateczl.controller;

public interface Publisher {
    
    void subscribe(Observer o);
    void unsubscribe(Observer o);
    void changeState();

}
