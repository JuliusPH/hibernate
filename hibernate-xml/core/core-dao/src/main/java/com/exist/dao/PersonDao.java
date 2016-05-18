package com.exist.dao;

import java.util.List;
import com.exist.model.Person;
import com.exist.model.enums.Sort;

public interface PersonDao extends GenericDao<Person>{
	Person get(Long id);
    
    List<Person> list();
    
    List<Person> list(Sort sort);
}
