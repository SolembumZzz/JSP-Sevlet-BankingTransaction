package com.cg.service;

import com.cg.model.Customer;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ICustomerService {
    void insertCustomer(Customer customer) throws SQLException;

    Customer selectCustomer(int id);

    List<Customer> selectAllCustomers();

    boolean deleteCustomer(int id) throws SQLException;

    boolean updateCustomer(Customer customer) throws SQLException;

    boolean checkExistingEmail(String email);

    void deposit(int id, BigDecimal transacAmt);

    void withdraw(int id, BigDecimal transacAmt);

    boolean transfer(int id, int targetId, BigDecimal transacAmt);

    List<Customer> selectAllRecipients(int senderId);

    void suspend(int id);

    boolean isSuspended(int id);
}
