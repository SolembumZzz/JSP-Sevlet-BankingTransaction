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
                break;
            case "suspend":
                suspendCustomer(request, response);
                break;
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

        int error = 1;
        String message = "An error has occurred.";
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

        int error = 1;
        String message = "An error has occurred.";
        Customer customer = null;

        try {
            customer = customerService.returnValidatedCustomer(request.getParameter("id"));
            error = -1;

        } catch (NonExistingCustomer | NumberFormatException e) {
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
        List<String> emptyInput = new ArrayList<>();
        Customer customer = new Customer();

        try {
            Customer oldInfo = customerService.returnValidatedCustomer(request.getParameter("id"));
            request.setAttribute("customer", oldInfo);
            error = -1;

            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            if (fullName.equals(""))
                emptyInput.add("full name");
            customer.setFullName(fullName);

            if (email.equals(""))
                emptyInput.add("email");
            customer.setEmail(email);

            if (phone.equals(""))
                emptyInput.add("phone");
            customer.setPhone(phone);

            if (address.equals(""))
                emptyInput.add("address");
            customer.setAddress(address);

            request.setAttribute("customer", customer);

            if (emptyInput.size() > 0)
                throw new EmptyInputException("Empty input");

            if (!email.equals(oldInfo.getEmail()) && customerService.checkExistingEmail(email))
                throw new ExistingEmailException("Email existed");

            if (customerService.updateCustomer(customer)) {
                error = 0;
                message = "Updated successfully";
                request.setAttribute("customer", customer);
            }

        } catch (NonExistingCustomer | NumberFormatException e) {
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
            error = 2;
            message = "This customer has been suspended.";
        } catch (EmptyInputException e) {
            error = 2;
            message = "Please fill in " + ((emptyInput.size() > 1) ? "these fields: " : "this field: ")
                    + String.join(", ", emptyInput) + ".";
        } catch (ExistingEmailException e) {
            error = 2;
            message = "This email has been registered by other customers. Please enter a new email.";
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("message", message);
        request.setAttribute("error", error);
        dispatcher.forward(request, response);
    }

    private void showSuspendForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/suspend.jsp");

        int error = 1;
        String message = "An error has occurred.";
        Customer customer = null;

        try {
            customer = customerService.returnValidatedCustomer(request.getParameter("id"));
            error = -1;

        } catch (NonExistingCustomer | NumberFormatException e) {
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
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
        Customer customer = null;

        try {
            customer = customerService.returnValidatedCustomer(request.getParameter("id"));
            int id = customer.getId();

            customerService.suspend(id);
            customer = null;
            error = 0;
            message = "Suspend successfully!";

        } catch (NonExistingCustomer | NumberFormatException e) {
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
            message = "This customer has been suspended.";
        }

        request.setAttribute("customer", customer);
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void showDepositForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/deposit.jsp");

        int error = 1;
        String message = "An error has occurred.";
        Customer customer = null;

        try {
            customer = customerService.returnValidatedCustomer(request.getParameter("id"));
            error = -1;

        }catch (NonExistingCustomer | NumberFormatException e) {
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
            message = "This customer has already been suspended.";
        }

        request.setAttribute("customer", customer);
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }


    private void depositMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/deposit.jsp");

        int error = 1;
        String message = "An error has occurred.";
        Customer customer = null;

        try {
            customer = customerService.returnValidatedCustomer(request.getParameter("id"));
            int id = customer.getId();

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

        } catch (NonExistingCustomer | NumberFormatException e) {
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
            message = "This customer has been suspended.";
        } catch (InvalidNumberException e) {
            message = "Invalid number.";
            error = 2;
        } catch (EmptyInputException e) {
            message = "Transaction field cannot be left empty.";
            error = 2;
        } catch (TooLowTransactionException e) {
            message = "Transaction amount cannot be below 50";
        }

        request.setAttribute("customer", customer);
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void showWithdrawForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/withdraw.jsp");

        int error = 1;
        String message = "An error has occurred.";
        Customer customer = null;

        try {
            customer = customerService.returnValidatedCustomer(request.getParameter("id"));
            error = -1;

        } catch (NonExistingCustomer | NumberFormatException e) {
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
            message = "This customer has been suspended.";
        }

        request.setAttribute("customer", customer);
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void withdrawMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/withdraw.jsp");
        int error = 1;
        String message = "An error has occurred.";
        Customer customer = null;

        try {
            customer = customerService.returnValidatedCustomer(request.getParameter("id"));
            int id = customer.getId();
            request.setAttribute("customer", customer);
            BigDecimal currentBalance = customer.getBalance();

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

        } catch (NonExistingCustomer | NumberFormatException e) {
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
            message = "This customer has been suspended.";
        } catch (InvalidNumberException e) {
            message = "Invalid number.";
            error = 2;
        } catch (EmptyInputException e) {
            message = "Transaction field cannot be left empty.";
            error = 2;
        } catch (TooLowTransactionException e) {
            message = "Transaction amount cannot be below 50.";
            error = 2;
        } catch (TooHighTransactionException e) {
            message = "Transaction amount cannot exceed the current balance.";
            error = 2;
        }

        request.setAttribute("message", message);
        request.setAttribute("error", error);
        dispatcher.forward(request, response);
    }

    private void showTransferForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/transfer.jsp");

        int error = 1;
        String message = "An error has occurred.";
        Customer customer = null;

        try {
            customer = customerService.returnValidatedCustomer(request.getParameter("id"));
            error = -1;
            int id = customer.getId();
            request.setAttribute("customer", customer);

            List<Customer> recipients = customerService.selectAllRecipients(id);
            request.setAttribute("recipients", recipients);

            request.setAttribute("fees", new Transfer().getFeeRate());

        } catch (NonExistingCustomer | NumberFormatException e) {
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
            message = "This customer has been suspended.";
        }

        request.setAttribute("customer", customer);
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void transferMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/transfer.jsp");

        int error = 1;
        String message = "An error has occurred";
        Customer sender = null;
        request.setAttribute("fees", new Transfer().getFeeRate());

        try {
            sender = customerService.returnValidatedCustomer(request.getParameter("id"));
            int senderId = sender.getId();
            List<Customer> recipients = customerService.selectAllRecipients(senderId);

            request.setAttribute("customer", sender);
            request.setAttribute("recipients", recipients);

            BigDecimal senderBalance = sender.getBalance();

//          Validate Id
            String rawRepId = request.getParameter("recipientId");
            if (rawRepId == null)
                throw new EmptyInputException("Empty recipient");
            int recipientId = Integer.parseInt(rawRepId);
            request.setAttribute("currentRecipient", recipientId);

//          Validate Transferring Amount
            String unprocessedAmt = request.getParameter("transferAmt");
            if (!Validation.isNumeric(unprocessedAmt))
                throw new InvalidNumberException("Invalid num");
            BigDecimal transferAmt = new BigDecimal(unprocessedAmt);
            if (transferAmt.equals(BigDecimal.ZERO))
                throw new TooLowTransactionException("Empty input");

//          Validate Total Amount
            String unprocessedTotal = request.getParameter("totalTransaction");
            if (!Validation.isNumeric(unprocessedTotal))
                throw new InvalidNumberException("Invalid number");
            BigDecimal totalTransaction = new BigDecimal(unprocessedTotal);

            if (totalTransaction.compareTo(senderBalance) > 0)
                throw new TooHighTransactionException("Too high amount");

//          Transfer
            boolean transferred = customerService.transfer(senderId, recipientId, transferAmt);
            if (!transferred)
                throw new TransactionIncompletedException("Probably rollback");
            error = 0;
            message = "Transaction completed!";
            sender = customerService.selectCustomer(senderId);
            request.setAttribute("customer", sender);

        } catch (NonExistingCustomer | NumberFormatException e) {
            message = "This id does not exist.";
        } catch (SuspendedCustomerException e) {
            message = "This customer has been suspended.";
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
            message = "The total amount cannot exceed current balance.";
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
