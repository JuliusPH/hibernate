package com.exist.service.impl;

import com.exist.dao.AddressDao;
import com.exist.dao.impl.AddressDaoImpl;
import com.exist.model.Address;
import com.exist.service.AddressService;
import org.hibernate.SessionFactory;

public class AddressServiceImpl extends GenericServiceImpl<Address> implements AddressService{
    private AddressDao addressDao;
    
    public AddressServiceImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        addressDao = new AddressDaoImpl(sessionFactory);
    }
}
