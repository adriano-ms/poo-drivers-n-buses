package com.edu.fateczl.controller;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String msg){
        super(msg);
    }
}
