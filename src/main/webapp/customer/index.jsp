<%--
  Created by IntelliJ IDEA.
  User: XV
  Date: 31-May-22
  Time: 11:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <%@ include file="/layout/head.jsp" %>
    <title>Customer Management</title>
</head>
<body>
<div class="container">
    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h1>List of customers</h1>
            </div>
            <div class="col-sm-7">
                <a class="btn btn-outline-light" href="${pageContext.request.contextPath}/customers?action=create">
                    <i class="far fa-plus-square"></i>
                    <span>Add new customer</span>
                </a>
                <a class="btn btn-outline-light" href="">
                    <i class="fa-solid fa-clock-rotate-left"></i>
                    <span>Transfer history</span>
                </a>
            </div>
        </div>
    </div>

    <table class="table table-hover" id="CMTable">
        <thead>
        <tr>
            <th>#</th>
            <th>FullName</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Address</th>
            <th>Balance</th>
            <th colspan="5">Action</th>
        </tr>
        </thead>
        <tbody id="customerList">
        <c:forEach var="customer" items="${customerList}">
            <tr>
                <td>${customer.getId()}</td>
                <td>${customer.getFullName()}</td>
                <td>${customer.getEmail()}</td>
                <td>${customer.getPhone()}</td>
                <td>${customer.getAddress()}</td>
                <td>${customer.getBalance()}</td>
                <td>
                    <a class="btn btn-outline-secondary" title="Edit"
                       href="${pageContext.request.contextPath}/customers?action=edit&id=${customer.getId()}">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </a>
                </td>
                <td>
                    <a class="btn btn-outline-success" title="Deposit"
                       href="${pageContext.request.contextPath}/customers?action=deposit&id=${customer.getId()}">
                        <i class="fa-solid fa-plus"></i>
                    </a>
                </td>
                <td>
                    <a class="btn btn-outline-warning" title="Withdraw"
                       href="${pageContext.request.contextPath}/customers?action=withdraw&id=${customer.getId()}">
                        <i class="fa-solid fa-minus"></i>
                    </a>
                </td>
                <td>
                    <a class="btn btn-outline-primary" title="Transfer"
                       href="${pageContext.request.contextPath}/customers?action=transfer&id=${customer.getId()}">
                        <i class="fa-solid fa-arrow-right-arrow-left"></i>
                    </a>
                </td>
                <td>
                    <a class="btn btn-outline-danger" title="Suspend"
                       href="${pageContext.request.contextPath}/customers?action=suspend&id=${customer.getId()}">
                        <i class="fa-solid fa-ban"></i>
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="/layout/script.jsp" %>
</body>
</html>
