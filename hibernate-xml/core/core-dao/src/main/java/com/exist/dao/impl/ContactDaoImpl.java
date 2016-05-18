package com.exist.dao.impl;

import com.exist.dao.ContactDao;
import com.exist.model.Contact;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ContactDaoImpl extends GenericDaoImpl<Contact> implements ContactDao{

    public ContactDaoImpl(SessionFactory sessionFactory){
        super(sessionFactory);
    }
}
