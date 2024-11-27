package com.edu.fateczl.model.dao;

import java.util.List;

public interface IDao<T> {

    void insert(T obj) throws DbException;
    void update(T obj) throws DbException;
    void delete(long id) throws DbException;
    T findOne(long id) throws DbException, NotFoundException;
    List<T> findAll() throws DbException;
}
