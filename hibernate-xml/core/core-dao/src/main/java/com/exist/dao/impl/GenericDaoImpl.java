package com.exist.dao.impl;

import com.exist.dao.GenericDao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class GenericDaoImpl<E> implements GenericDao<E>{
    private SessionFactory sessionFactory;
    
    public GenericDaoImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    
    protected Session getCurrentSession(){
        return sessionFactory.openSession();
    }
    
    @Override
    public boolean add(E entity){
        Session session = getCurrentSession();
        Transaction transaction = null;
        boolean isSuccessful = true;
        try{
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        }
        catch(HibernateException e){
            if(transaction != null){
                transaction.rollback();
            }
            isSuccessful = false;
        }
        finally{
            session.close();    
        }
        return isSuccessful;
    }
    
    @Override
    public boolean update(E entity){
        Session session = getCurrentSession();
        Transaction transaction = null;
        boolean isSuccessful = true;
        try{
            transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            transaction.commit();
        }
        catch(HibernateException e){
            if(transaction != null){
                transaction.rollback();
            }
            isSuccessful = false;
        }
        finally{
            session.close();    
        }
        return isSuccessful;
    }
    
    @Override
    public boolean remove(E entity){
        Session session = getCurrentSession();
        Transaction transaction = null;
        boolean isSuccessful = true;
        try{
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        }
        catch(HibernateException e){
            if(transaction != null){
                transaction.rollback();
            }
            isSuccessful = false;
        }
        finally{
            session.close();    
        }
        return isSuccessful;
    }
}
