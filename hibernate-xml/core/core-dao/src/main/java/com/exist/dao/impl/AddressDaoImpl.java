package com.exist.dao.impl;

import com.exist.dao.AddressDao;
import com.exist.model.Address;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AddressDaoImpl extends GenericDaoImpl<Address> implements AddressDao{
    public AddressDaoImpl(SessionFactory sessionFactory){
        super(sessionFactory);
    }
    
    @Override
    public boolean remove(Long personId){
        Session session = getCurrentSession();
        String hql = "FROM com.exist.model.Address a WHERE a.id=(SELECT address_id FROM com.exist.model.Person WHERE :id)";
        Query query = session.createQuery(hql);
        query.setParameter("id", personId);
        return query.executeUpdate() > 0;
    }
}
