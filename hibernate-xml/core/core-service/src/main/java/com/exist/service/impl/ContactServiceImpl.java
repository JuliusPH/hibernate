package com.exist.service.impl;

import com.exist.dao.ContactDao;
import com.exist.dao.impl.ContactDaoImpl;
import com.exist.model.Contact;
import com.exist.service.ContactService;
import org.hibernate.SessionFactory;

public class ContactServiceImpl extends GenericServiceImpl<Contact> implements ContactService{
    private ContactDao contactDao;
    
    public ContactServiceImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        contactDao = new ContactDaoImpl(sessionFactory);
    }
}
