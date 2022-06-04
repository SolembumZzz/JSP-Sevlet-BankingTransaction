package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.service.CustomerService;
import com.cg.service.ICustomerService;
import com.cg.service.ITransferService;
import com.cg.service.TransferService;
import com.cg.utils.MySQLConnectionUtils;
import com.cg.utils.Validation;
import com.cg.utils.exception.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CustomerServlet", value = "/customers")
public class CustomerServlet extends HttpServlet {

    ICustomerService customerService = new CustomerService();
    ITransferService transferService = new TransferService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null)
            action = "";
        switch (action) {
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "deposit":
                showDepositForm(request, response);
                break;
            case "withdraw":
                showWithdrawForm(request, response);
                break;
            case "transfer":
                showTransferForm(request, response);
                break;
            case "transfer-history":
                showTransferHistory(request, response);
            case "suspend":
                showSuspendForm(request, response);
                break;
            default:
                listCustomers(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null)
            action = "";
        switch (action) {
            case "create":
                createCustomer(request, response);
                break;
            case "edit":
                editCustomer(request, response);
                break;
            case "deposit":
                depositMoney(request, response);
                break;
            case "withdraw":
                withdrawMoney(request, response);
                break;
            case "transfer":
                transferMoney(request, response);
            case "suspend":
                suspendCustomer(request, response);
            default:
                break;
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/index.jsp");

        List<Customer> customerList = customerService.selectAllCustomers();

        request.setAttribute("customerList", customerList);
        dispatcher.forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/create.jsp");

        dispatcher.forward(request, response);
    }

    private void createCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("customer/create.jsp");

        String message = "An error has occurred.";
        int error = 1;
        List<String> emptyInput = new ArrayList<>();
        Customer newCustomer = new Customer();
        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            if (fullName.equals(""))
                emptyInput.add("full name");
            newCustomer.setFullName(fullName);

            if (email.equals(""))
                emptyInput.add("email");
            newCustomer.setEmail(email);

            if (phone.equals(""))
                emptyInput.add("phone");
            newCustomer.setPhone(phone);

            if (address.equals(""))
                emptyInput.add("address");
            newCustomer.setAddress(address);

            request.setAttribute("customer", newCustomer);
            if (emptyInput.size() > 0)
                throw new EmptyInputException("Empty input");

            if (customerService.checkExistingEmail(email))
                throw new ExistingEmailException("Email existed");

            customerService.insertCustomer(newCustomer);
            message = "New customer has been added!";
            error = 0;
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        } catch (EmptyInputException e) {
            error = 2;
            message = "Please fill in " + ((emptyInput.size() > 1) ? "these fields: " : "this field: ") +
                    String.join(", ", emptyInput) + ".";
        } catch (ExistingEmailException e) {
            error = 2;
            message = "Email registered. Please enter a new email.";
        }
        request.setAttribute("customer", newCustomer);
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/edit.jsp");

        int error = -1;
        String message = "An error has occurred.";
        Customer customer = null;

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            customer = customerService.selectCustomer(id);

            if (customer == null)
                throw new NonExistingCustomer("Non-existing customer.");

            if (customerService.isSuspended(id))
                throw new SuspendedCustomerException("Suspended customer");
        } catch (NonExistingCustomer e) {
            error = 1;
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
            error = 2;
            message = "This customer has been suspended.";
        }

        request.setAttribute("customer", customer);
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void editCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/edit.jsp");

        int error = 1;
        String message = "An error has occurred.";

        int id = Integer.parseInt(request.getParameter("id"));
        Customer oldInfo = customerService.selectCustomer(id);
        request.setAttribute("customer", oldInfo);

        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            if (fullName.equals("") || email.equals("") || phone.equals("") || address.equals(""))
                throw new EmptyInputException("Empty input");
            if (!email.equals(customerService.selectCustomer(id).getEmail())
                    && customerService.checkExistingEmail(email))
                throw new ExistingEmailException("Email existed");

            Customer newInfo = new Customer(id, fullName, email, phone, address);

            if (customerService.updateCustomer(newInfo)) {
                error = 0;
                message = "Updated successfully";
                request.setAttribute("customer", newInfo);
            }
        } catch (EmptyInputException e) {
            error = 2;
            message = "One or more field is left empty. Please fill them up.";
        } catch (ExistingEmailException e) {
            error = 2;
            message = "This email has been registered by other customers. Please enter a new email.";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("message", message);
        request.setAttribute("error", error);
        dispatcher.forward(request, response);
    }

    private void showSuspendForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/suspend.jsp");

        int error = -1;
        String message = "An error has occurred.";
        Customer customer = null;

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            customer = customerService.selectCustomer(id);

            if (customer == null)
                throw new NonExistingCustomer("Non-existing customer");
            if (customerService.isSuspended(id))
                throw new SuspendedCustomerException("Suspended customer");
            request.setAttribute("customer", customer);
        } catch (NonExistingCustomer e) {
            error = 1;
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
            error = 1;
            message = "This customer has already been suspended.";
        }

        request.setAttribute("customer", customer);
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void suspendCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/suspend.jsp");

        int error = 1;
        String message = "An error has occurred.";

        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);

        customerService.suspend(id);
        error = 0;
        message = "Suspend successfully!";

        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void showDepositForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/deposit.jsp");

        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);

        dispatcher.forward(request, response);
    }


    private void depositMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/deposit.jsp");

        int error = 1;
        String message = "An error has occurred.";

        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);

        try {
            BigDecimal transacAmt;
            String unprocessedAmt = request.getParameter("transacAmt");
            if (!Validation.isNumeric(unprocessedAmt))
                throw new InvalidNumberException("Invalid num");
            transacAmt = new BigDecimal(unprocessedAmt);
            if (transacAmt.equals(BigDecimal.ZERO))
                throw new EmptyInputException("Empty input");
            if (transacAmt.compareTo(new BigDecimal("50")) < 0)
                throw new TooLowTransactionException("Amount too low");
            customerService.deposit(id, transacAmt);

            customer = customerService.selectCustomer(id);
            request.setAttribute("customer", customer);
            error = 0;
            message = "Transaction completed!";
        } catch (InvalidNumberException e) {
            message = "Invalid number.";
            error = 2;
        } catch (EmptyInputException e) {
            message = "Transaction field cannot be left empty.";
            error = 2;
        } catch (TooLowTransactionException e) {
            message = "Transaction amount cannot be below 50";
        }

        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void showWithdrawForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/withdraw.jsp");

        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);

        dispatcher.forward(request, response);
    }

    private void withdrawMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/withdraw.jsp");
        int error = 1;
        String message = "An error has occurred.";

        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);
        BigDecimal currentBalance = customer.getBalance();

        try {
            BigDecimal transacAmt;
            String unprocessedAmt = request.getParameter("transacAmt");
            if (!Validation.isNumeric(unprocessedAmt))
                throw new InvalidNumberException("Invalid num");
            transacAmt = new BigDecimal(unprocessedAmt);
            if (transacAmt.equals(BigDecimal.ZERO))
                throw new EmptyInputException("Empty input");
            if (transacAmt.compareTo(new BigDecimal("50")) < 0)
                throw new TooLowTransactionException("Amount too low");
            if (transacAmt.compareTo(currentBalance) > 0)
                throw new TooHighTransactionException("Amount too high");
            customerService.withdraw(id, transacAmt);

            customer = customerService.selectCustomer(id);
            request.setAttribute("customer", customer);
            error = 0;
            message = "Transaction completed!";
        } catch (InvalidNumberException e) {
            message = "Invalid number.";
            error = 2;
        } catch (EmptyInputException e) {
            message = "Transaction field cannot be left empty.";
            error = 2;
        } catch (TooLowTransactionException e) {
            message = "Transaction amount cannot be below 50.";
        } catch (TooHighTransactionException e) {
            message = "Transaction amount cannot exceed the current balance (" + currentBalance + ").";
        }

        request.setAttribute("message", message);
        request.setAttribute("error", error);
        dispatcher.forward(request, response);
    }

    private void showTransferForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/transfer.jsp");

        int id = Integer.parseInt(request.getParameter("id"));

        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);

        List<Customer> recipients = customerService.selectAllRecipients(id);
        request.setAttribute("recipients", recipients);

        request.setAttribute("fees", new Transfer() {
        }.getFeeRate());

        dispatcher.forward(request, response);
    }

    private void transferMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/transfer.jsp");

        int error = 1;
        String message = "An error has occurred";

        int id = Integer.parseInt(request.getParameter("id"));
        Customer sender = customerService.selectCustomer(id);
        List<Customer> recipients = customerService.selectAllRecipients(id);

        request.setAttribute("customer", sender);
        request.setAttribute("recipients", recipients);
        request.setAttribute("fees", new Transfer().getFeeRate());

        BigDecimal senderBalance = sender.getBalance();

        try {
//          Validate Id
            String unprocessedRepId = request.getParameter("recipientId");
            if (unprocessedRepId == null)
                throw new EmptyInputException("Empty recipient");
            int recipientId = Integer.parseInt(unprocessedRepId);
            request.setAttribute("currentRecipient", recipientId);

//          Validate Transferring Amount
            String unprocessedAmt = request.getParameter("transferAmt");
            if (unprocessedAmt == null || unprocessedAmt.equals(""))
                unprocessedAmt = "0";
            BigDecimal transferAmt = new BigDecimal(unprocessedAmt);
            if (transferAmt.equals(BigDecimal.ZERO))
                throw new TooLowTransactionException("Too low amount");

//          Validate Total Amount
            String unprocessedTotal = request.getParameter("totalTransaction");
            if (!Validation.isNumeric(unprocessedTotal))
                throw new InvalidNumberException("Invalid number");
            BigDecimal totalTransaction = new BigDecimal(unprocessedTotal);

            if (totalTransaction.compareTo(senderBalance) > 0)
                throw new TooHighTransactionException("Too high amount");

//          Transfer
            boolean transferred = customerService.transfer(id, recipientId, transferAmt);
            if (!transferred)
                throw new TransactionIncompletedException("Probably rollback");

            sender = customerService.selectCustomer(id);
            request.setAttribute("customer", sender);
            error = 0;
            message = "Transaction completed!";
        } catch (EmptyInputException e) {
            error = 2;
            message = "Recipient field cannot be empty.";
        } catch (TooLowTransactionException e) {
            error = 2;
            message = "Transferring amount invalid. The amount must be bigger than 0.";
        } catch (InvalidNumberException e) {
            error = 2;
            message = "Invalid number";
        } catch (TooHighTransactionException e) {
            error = 2;
            message = "The total amount cannot exceed current balance (" + senderBalance + ").";
        } catch (TransactionIncompletedException e) {
            message = "An error has occurred. Transaction uncompleted.";
        }

        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void showTransferHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/transfer-history.jsp");

        List<Transfer> transferHistory = transferService.selectAllTransfers();
        BigDecimal totalFee = transferService.calculateTotalFees();

        request.setAttribute("transferHistory", transferHistory);
        request.setAttribute("totalFee", totalFee);
        dispatcher.forward(request, response);
    }
}
