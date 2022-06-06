package com.cg.service;

import com.cg.model.Customer;
import com.cg.utils.MySQLConnectionUtils;
import com.cg.utils.exception.NonExistingCustomer;
import com.cg.utils.exception.SuspendedCustomerException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService implements ICustomerService {
    private static final String INSERT_CUSTOMER_SQL = "{CALL banking_transaction.sp_insert_customer(?,?,?,?)}";
    private static final String SELECT_CUSTOMER_BY_ID
            = "{CALL banking_transaction.sp_select_customer(?)}";
    private static final String SELECT_ALL_CUSTOMERS = "SELECT id, full_name, email, phone, address, balance "
            + "FROM customers "
            + "WHERE customers.deleted = 0";

    private static final String SELECT_ALL_RECIPIENTS = "{CALL banking_transaction.sp_select_all_recipients(?)}";
    private static final String DELETE_CUSTOMERS_SQL = "DELETE FROM customers "
            + "WHERE id = ?;";
    private static final String UPDATE_CUSTOMERS_SQL = "{CALL banking_transaction.sp_update_customer(?,?,?,?,?)}";

    private static final String DEPOSIT_MONEY_SQL = "{CALL banking_transaction.sp_deposit(?,?)}";

    private static final String WITHDRAW_MONEY_SQL = "{CALL banking_transaction.sp_withdraw(?,?)}";

    private static final String TRANSFER_MONEY_SQL = "{CALL banking_transaction.sp_transfer(?,?,?,?)}";

    private static final String SUSPEND_CUSTOMER_SQL = "{CALL banking_transaction.sp_suspend(?)}";

    private static final String CHECK_IF_SUSPENDED_SQL = "{CALL banking_transaction.sp_check_if_suspended(?,?)}";

    @Override
    public void insertCustomer(Customer customer) {
        System.out.println(INSERT_CUSTOMER_SQL);
        try {
            Connection con = MySQLConnectionUtils.getConnection();
            CallableStatement cs = con.prepareCall(INSERT_CUSTOMER_SQL);
            cs.setString(1, customer.getFullName());
            cs.setString(2, customer.getEmail());
            cs.setString(3, customer.getPhone());
            cs.setString(4, customer.getAddress());
            System.out.println(cs);

            cs.execute();
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
    }

    @Override
    public Customer selectCustomer(int id) {
        Customer customer = null;
        try {
            Connection con = MySQLConnectionUtils.getConnection();
            CallableStatement cs = con.prepareCall(SELECT_CUSTOMER_BY_ID);
            cs.setInt(1, id);
            System.out.println(cs);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                BigDecimal balance = new BigDecimal(rs.getLong("balance"));
                customer = new Customer(id, fullName, email, phone, address, balance);
            }
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
        return customer;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        try {
            Connection con = MySQLConnectionUtils.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_ALL_CUSTOMERS);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                BigDecimal balance = new BigDecimal(rs.getLong("balance"));

                customerList.add(new Customer(id, fullName, email, phone, address, balance));
            }

        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
        return customerList;
    }

    @Override
    public boolean deleteCustomer(int id) throws SQLException {
        boolean rowDeleted = false;
        try {
            Connection con = MySQLConnectionUtils.getConnection();
            PreparedStatement ps = con.prepareStatement(DELETE_CUSTOMERS_SQL);
            ps.setInt(1, id);
            rowDeleted = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
        return rowDeleted;
    }

    @Override
    public boolean updateCustomer(Customer customer) throws SQLException {
        boolean rowUpdated = false;
        try {
            Connection con = MySQLConnectionUtils.getConnection();
            CallableStatement cs = con.prepareCall(UPDATE_CUSTOMERS_SQL);
            cs.setInt(1, customer.getId());
            cs.setString(2, customer.getFullName());
            cs.setString(3, customer.getEmail());
            cs.setString(4, customer.getPhone());
            cs.setString(5, customer.getAddress());

            rowUpdated = (!cs.execute());
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
        return rowUpdated;
    }

    @Override
    public boolean checkExistingEmail(String email) {
        List<Customer> customerList = selectAllCustomers();
        for (Customer customer : customerList) {
            if (email.equals(customer.getEmail()))
                return true;
        }
        return false;
    }

    @Override
    public void deposit(int id, BigDecimal transacAmt) {
        try {
            Connection con = MySQLConnectionUtils.getConnection();
            CallableStatement cs = con.prepareCall(DEPOSIT_MONEY_SQL);
            cs.setInt(1, id);
            cs.setBigDecimal(2, transacAmt);

            cs.execute();
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
    }

    @Override
    public void withdraw(int id, BigDecimal transacAmt) {
        try {
            Connection con = MySQLConnectionUtils.getConnection();
            CallableStatement cs = con.prepareCall(WITHDRAW_MONEY_SQL);
            cs.setInt(1, id);
            cs.setBigDecimal(2, transacAmt);

            cs.execute();
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
    }

    @Override
    public boolean transfer(int id, int targetId, BigDecimal transacAmt) {
        boolean rowUpdated = false;
        try {
            Connection con = MySQLConnectionUtils.getConnection();
            CallableStatement cs = con.prepareCall(TRANSFER_MONEY_SQL);
            cs.setInt(1, id);
            cs.setInt(2, targetId);
            cs.setBigDecimal(3, transacAmt);

            cs.execute();

            rowUpdated = cs.getBoolean(4);
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
        System.out.println(rowUpdated);
        return rowUpdated;
    }

    @Override
    public List<Customer> selectAllRecipients(int senderId) {
        List<Customer> customerList = new ArrayList<>();
        try {
            Connection con = MySQLConnectionUtils.getConnection();
            CallableStatement cs = con.prepareCall(SELECT_ALL_RECIPIENTS);
            cs.setInt(1, senderId);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String fullName = rs.getString("full_name");

                customerList.add(new Customer(id, fullName));
            }

        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
        return customerList;
    }

    @Override
    public void suspend(int id) {
        try {
            Connection conn = MySQLConnectionUtils.getConnection();
            CallableStatement cs = conn.prepareCall(SUSPEND_CUSTOMER_SQL);
            cs.setInt(1, id);

            cs.execute();
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
    }

    @Override
    public boolean isSuspended(int id) {
        boolean suspended = false;
        try {
            Connection conn = MySQLConnectionUtils.getConnection();
            CallableStatement cs = conn.prepareCall(CHECK_IF_SUSPENDED_SQL);
            cs.setInt(1, id);

            cs.execute();

            suspended = cs.getBoolean(2);
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
        return suspended;
    }

    @Override
    public Customer returnValidatedCustomer(String strId) throws NumberFormatException, NonExistingCustomer, SuspendedCustomerException {
        int id = Integer.parseInt(strId);
        Customer customer = selectCustomer(id);
        if (customer == null)
            throw new NonExistingCustomer("Non-existing customer");
        if (isSuspended(id))
            throw new SuspendedCustomerException("Customer suspended");
        return customer;
    }
}
