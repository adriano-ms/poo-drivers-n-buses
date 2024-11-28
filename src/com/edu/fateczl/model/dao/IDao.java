package com.edu.fateczl.model.dao;

import java.util.List;

import com.edu.fateczl.controller.Publisher;

public interface IDao<T> extends Publisher{

    void insert(T obj) throws DbException;
    void update(T obj) throws DbException;
    void delete(long id) throws DbException;
    T findOne(long id) throws DbException, NotFoundException;
    List<T> findAll() throws DbException;
}
