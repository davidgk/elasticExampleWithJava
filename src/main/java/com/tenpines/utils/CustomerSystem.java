package com.tenpines.utils;
import com.tenpines.model.Customer;

import java.util.List;

public interface CustomerSystem {

    void closeSessionAndCommit();

    void saveCustomer(Customer newCustomer);

    List getAllCustomers();

    Customer findCustomerByName(String pepe);

    void initDbConfiguration();

    void removeAll();
}
