package com.exist.dao;

public interface GenericDao<E>{
    boolean add(E entity);
	
	boolean update(E entity);
	
	boolean remove(E entity);
}
