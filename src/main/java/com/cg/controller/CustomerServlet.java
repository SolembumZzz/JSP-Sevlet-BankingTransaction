package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.service.CustomerService;
import com.cg.service.ICustomerService;
import com.cg.utils.MySQLConnectionUtils;
import com.cg.utils.exception.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CustomerServlet", value = "/customers")
public class CustomerServlet extends HttpServlet {

    ICustomerService customerService = new CustomerService();

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
            case "suspend":
//                showSuspendForm(request, response);
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
        String message = "An error has occurred.";
        int error = 1;
        RequestDispatcher dispatcher = request.getRequestDispatcher("customer/create.jsp");
        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            if (fullName.equals("") || email.equals("") || phone.equals("") || address.equals(""))
                throw new EmptyInputException("Empty input");
            if (customerService.checkExistingEmail(email))
                throw new ExistingEmailException("Email existed");
            Customer newCustomer = new Customer(fullName, email, phone, address);
            customerService.insertCustomer(newCustomer);
            message = "New customer has been added!";
            error = 0;
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        } catch (EmptyInputException e) {
            error = 2;
            message = "One or more field is left empty. Please fill them up.";
        } catch (ExistingEmailException e) {
            error = 2;
            message = "Email registered. Please enter a new email.";
        }
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/edit.jsp");
        dispatcher.forward(request, response);
    }

    private void editCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            try {
                if (customerService.updateCustomer(newInfo)) {
                    error = 0;
                    message = "Updated successfully";
                    request.setAttribute("customer", newInfo);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (EmptyInputException e) {
            error = 2;
            message = "One or more field is left empty. Please fill them up.";
        } catch (ExistingEmailException e) {
            error = 2;
            message = "This email has been registered by other customers. Please enter a new email.";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/edit.jsp");
        request.setAttribute("message", message);
        request.setAttribute("error", error);
        dispatcher.forward(request, response);
    }

    private void showDepositForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/deposit.jsp");
        dispatcher.forward(request, response);
    }

    private void depositMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int error = 1;
        String message = "An error has occurred.";

        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);

        try {
            long transacAmt;
            String unprocessedAmt = request.getParameter("transacAmt");
            if (unprocessedAmt.equals(""))
                unprocessedAmt = "0";
            transacAmt = Long.parseLong(unprocessedAmt);
            if (transacAmt == 0)
                throw new EmptyInputException("Empty input");
            if (transacAmt < 50)
                throw new TooLowTransactionException("Amount too low");
            customerService.deposit(id, transacAmt);

            customer = customerService.selectCustomer(id);
            request.setAttribute("customer", customer);
            error = 0;
            message = "Transaction completed!";
        } catch (EmptyInputException e) {
            message = "Transaction field cannot be left empty.";
            error = 2;
        } catch (TooLowTransactionException e) {
            message = "Transaction amount cannot be below 50";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/deposit.jsp");
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
//        response.sendRedirect("/customers?action=deposit&id=" + id);
    }

    private void showWithdrawForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/withdraw.jsp");
        dispatcher.forward(request, response);
    }

    private void withdrawMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int error = 1;
        String message = "An error has occurred.";

        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);
        long currentBalance = customer.getBalance();

        try {
            long transacAmt;
            String unprocessedAmt = request.getParameter("transacAmt");
            if (unprocessedAmt.equals(""))
                unprocessedAmt = "0";
            transacAmt = Long.parseLong(unprocessedAmt);
            if (transacAmt == 0)
                throw new EmptyInputException("Empty input");
            if (transacAmt < 50)
                throw new TooLowTransactionException("Amount too low");
            if (transacAmt > currentBalance)
                throw new TooHighTransactionException("Amount too high");
            customerService.withdraw(id, transacAmt);

            customer = customerService.selectCustomer(id);
            request.setAttribute("customer", customer);
            error = 0;
            message = "Transaction completed!";
        } catch (EmptyInputException e) {
            message = "Transaction field cannot be left empty.";
            error = 2;
        } catch (TooLowTransactionException e) {
            message = "Transaction amount cannot be below 50";
        } catch (TooHighTransactionException e) {
            message = "Transaction amount cannot exceed current balance (" + currentBalance + ").";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/withdraw.jsp");
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

    private void showTransferForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        Customer customer = customerService.selectCustomer(id);
        request.setAttribute("customer", customer);

        List<Customer> recipients = customerService.selectAllRecipients(id);
        request.setAttribute("recipients", recipients);

        request.setAttribute("fees", new Transfer() {
        }.getFees());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/transfer.jsp");
        dispatcher.forward(request, response);
    }

    private void transferMoney(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int error = 1;
        String message = "An error has occurred";

        int id = Integer.parseInt(request.getParameter("id"));
        Customer sender = customerService.selectCustomer(id);
        List<Customer> recipients = customerService.selectAllRecipients(id);

        request.setAttribute("customer", sender);
        request.setAttribute("recipients", recipients);
        request.setAttribute("fees", new Transfer() {
        }.getFees());

        long senderBalance = sender.getBalance();
        long transferAmt = Long.parseLong(request.getParameter("transferAmt"));

        try {
            String unprocessedRepId = request.getParameter("recipientId");
            if (unprocessedRepId.equals(""))
                throw new EmptyInputException("Empty recipient");
            int recipientId = Integer.parseInt(unprocessedRepId);
            request.setAttribute("currentRecipient", recipientId);

            String unprocessedTotal = request.getParameter("totalTransaction");
            if (unprocessedTotal.equals(""))
                unprocessedTotal = "0";
            long totalTransaction = Long.parseLong(unprocessedTotal);
            if (totalTransaction == 0)
                throw new TooLowTransactionException("Too low amount");
            if (totalTransaction > senderBalance)
                throw new TooHighTransactionException("Too high amount");

            boolean transferred = customerService.transfer(id, recipientId, transferAmt);
            if (!transferred) {
                throw new TransactionIncompletedException("Probably rollback");
            }
            sender = customerService.selectCustomer(id);
            request.setAttribute("customer", sender);
            error = 0;
            message = "Transaction completed!";
        } catch (EmptyInputException e) {
            error = 2;
            message = "Recipient field cannot be empty.";
        } catch (TooLowTransactionException e) {
            error = 2;
            message = "Transfer amount cannot be left empty.";
        } catch (TooHighTransactionException e) {
            error = 2;
            message = "The total amount cannot exceed current balance (" + senderBalance + ").";
        } catch (TransactionIncompletedException e) {
            message = "An error has occurred. Transaction uncompleted.";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/transfer.jsp");
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        dispatcher.forward(request, response);
    }

}
