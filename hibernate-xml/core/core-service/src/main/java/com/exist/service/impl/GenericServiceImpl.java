package com.exist.service.impl;

import com.exist.service.GenericService;
import com.exist.dao.GenericDao;
import com.exist.dao.impl.GenericDaoImpl;
import org.hibernate.SessionFactory;

public class GenericServiceImpl<E> implements GenericService<E> {
    private GenericDao<E> genericDao;
 
    public GenericServiceImpl(SessionFactory sessionFactory) {
        genericDao = new GenericDaoImpl(sessionFactory);
    }
    
    @Override
    public boolean add(E entity) {
        return genericDao.add(entity);
    }
 
    @Override
    public boolean update(E entity) {
        return genericDao.update(entity);
    }
 
    @Override
    public boolean remove(E entity) {
        return genericDao.remove(entity);
    }
}
