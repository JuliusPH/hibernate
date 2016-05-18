package com.exist.service.impl;

import com.exist.dao.PersonDao;
import com.exist.dao.impl.PersonDaoImpl;
import com.exist.model.Person;
import com.exist.model.enums.Sort;
import com.exist.service.PersonService;
import java.util.List;
import org.hibernate.SessionFactory;

public class PersonServiceImpl extends GenericServiceImpl<Person> implements PersonService{
    private PersonDao personDao;
    
    public PersonServiceImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        personDao = new PersonDaoImpl(sessionFactory);
    }
 
    @Override
    public Person get(Long id) {
        return personDao.get(id);
    }
 
    @Override
    public List<Person> list() {
        return personDao.list();
    }
    
    @Override
    public List<Person> list(Sort sort){
        return personDao.list(sort);
    }
}
