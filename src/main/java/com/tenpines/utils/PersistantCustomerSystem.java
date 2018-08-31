package com.tenpines.utils;

import com.tenpines.model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.List;

public class PersistantCustomerSystem implements CustomerSystem{
    private Session session;

    private PersistantCustomerSystem(){
    }

    public static CustomerSystem initialize() {
        CustomerSystem customerSystem = new PersistantCustomerSystem();
        customerSystem.initDbConfiguration();
        return customerSystem;
    }

    public void initDbConfiguration() {
        Configuration configuration = new Configuration();
        configuration.configure();

        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Override
    public void removeAll() {
        this.getAllCustomers().forEach(customer -> session.delete(customer));
    }

    public Customer findCustomerByName(String name) {
        return (Customer) session.createCriteria(Customer.class).add(Restrictions.eq("firstName", name)).uniqueResult();
    }

    public void closeSessionAndCommit() {
        session.getTransaction().commit();
        session.close();
    }


    public List getAllCustomers() {
        return session.createCriteria(Customer.class).list();
    }

    public void saveCustomer(Customer customer) {
        session.persist(customer);
    }
}
