package com.exist.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil{
    private static final SessionFactory sessionFactory;
    
    static {
        try{
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
            .configure("hibernate.cfg.xml")
            .build();

            Metadata metadata = new MetadataSources(standardRegistry)
            .getMetadataBuilder()
            .build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } 
        catch(Throwable ex){
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory(){
     return sessionFactory;
    }
}
