package com.cg.service;

import com.cg.model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface ICustomerService {
    void insertCustomer(Customer customer) throws SQLException;

    Customer selectCustomer(int id);

    List<Customer> selectAllCustomers();

    boolean deleteCustomer(int id) throws SQLException;

    boolean updateCustomer(Customer customer) throws SQLException;

    boolean checkExistingEmail(String email);

    void deposit(int id, Long transacAmt);

    void withdraw(int id, Long transacAmt);

    boolean transfer(int id, int targetId, Long transacAmt);

    List<Customer> selectAllRecipients(int senderId);

    void suspend(int id);
}
