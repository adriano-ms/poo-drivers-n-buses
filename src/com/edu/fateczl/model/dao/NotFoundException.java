package com.edu.fateczl.model.dao;

public class NotFoundException extends RuntimeException {
    
    public NotFoundException(String msg){
        super(msg);
    }

}
