package com.exist.service;

public interface GenericService<E> {
    boolean add(E entity);
	
	boolean update(E entity);
	
	boolean remove(E entity);
}
