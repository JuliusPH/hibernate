package com.exist.service;

import java.util.List;
import com.exist.model.Person;
import com.exist.model.enums.Sort;

public interface PersonService extends GenericService<Person>{
	Person get(Long id);
    
    List<Person> list();
    
    List<Person> list(Sort sort);
}
