package com.exist.dao.impl;

import com.exist.dao.PersonDao;
import com.exist.model.Person;
import com.exist.model.enums.Sort;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class PersonDaoImpl extends GenericDaoImpl<Person> implements PersonDao{

    public PersonDaoImpl(SessionFactory sessionFactory){
        super(sessionFactory);
    }
    
    @Override
    public Person get(Long id){
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Person person = null;
        try{
            person = (Person)session.get(Person.class, id);
            transaction.commit();
        }
        catch(HibernateException e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
        finally{
            session.close();
        }
        return person;
    }
    
    @Override
    public List<Person> list(){
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List list = null;
        try{
            list = session.createCriteria(Person.class).list();
            transaction.commit();
        }
        catch(HibernateException e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
        finally{
            session.close();
        }
        return list;
    }
    
    @Override
    public List<Person> list(Sort sort){
        Session session = getCurrentSession();
        List<Person> list = list();
        String hql = "";
        Query query = null;
        switch(sort){
            case Gwa:
                list.sort((person1, person2) -> person1.getGwa().compareTo(person2.getGwa()));
                break;
            case DateHired:
                hql = "FROM com.exist.model.Person ORDER BY date_hired";
                query = session.createQuery(hql);
                list = query.list();
                break;
            case LastName:
                hql = "FROM com.exist.model.Person ORDER BY last_name";
                query = session.createQuery(hql);
                list = query.list();
                break;
            default:
                break;
        }
        session.close();
        return list;
    }
}
