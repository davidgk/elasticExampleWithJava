package com.tenpines.utils;

import com.tenpines.model.Customer;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PersistantCustomerSystemTest {

    final static Logger logger = Logger.getLogger(PersistantCustomerSystemTest.class);
    private CustomerSystem customerSystemByH2;
    private CustomerSystem customerSystemByEs;



    @After
    public void tearDown(){
        clean(customerSystemByH2);
        clean(customerSystemByEs);
    }

    private void clean(CustomerSystem customerSystem) {
        if (customerSystem != null){
            customerSystem.removeAll();
            customerSystem.closeSessionAndCommit();
        }
    }

    @Test
    public void getAllCustomersByH2AndHibernate() {
        customerSystemByH2 = PersistantCustomerSystem.initialize();
        for (int i = 0; i < getMaxToIterate(); i++) {
            Customer customer = Customer.create("hernan", "wilki", "D", "1234");
            customerSystemByH2.saveCustomer(customer);;
        }
        assertEquals(getMaxToIterate(), customerSystemByH2.getAllCustomers().size());
    }

    private int getMaxToIterate() {
        return 1000;
    }

    @Test
    public void getAllCustomersByElastic() {
        logger.info("*********************************************************");
        logger.info("Init");
        for (int i = 0; i < getMaxToIterate(); i++) {
            Customer customer = Customer.createForElastic("hernan", "wilki", "D", "1234", i);
            customerSystemByEs.saveCustomer(customer);
        }
        assertEquals(getMaxToIterate(), customerSystemByEs.getAllCustomers().size());

    }

}