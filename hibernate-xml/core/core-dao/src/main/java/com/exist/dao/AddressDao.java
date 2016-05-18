package com.exist.dao;

import com.exist.model.Address;

public interface AddressDao extends GenericDao<Address>{
    boolean remove(Long personId);
}
