<%--
  Created by IntelliJ IDEA.
  User: XV
  Date: 01-Jun-22
  Time: 10:44 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <%@ include file="/layout/head.jsp" %>
    <title>Withdraw</title>
</head>
<body>
<div class="container">
    <div class="table-title mb-4">
        <div class="row">
            <div class="col-sm-5">
                <h1>Withdraw</h1>
            </div>
            <%@ include file="/layout/nav-list-of-customers.jsp" %>
        </div>
    </div>
    <form id="frmWithdraw" method="post" novalidate="novalidate">
        <fieldset class="row g-3">
            <div class="col-md-6">
                <label class="form-label">Customer ID</label>
                <input class="form-control" type="number" name="customerID" id="customerID"
                       value="${customer.getId()}" readonly>
            </div>
            <div class="col-md-6">
                <label class="form-label">Full Name</label>
                <input class="form-control" type="text" name="fullName" id="fullName"
                       value="${customer.getFullName()}" readonly>
            </div>
            <div class="col-md-6">
                <label class="form-label">Current balance ($)</label>
                <input class="form-control" type="tel" name="currentBalance" id="currentBalance"
                       value="${customer.getBalance()}" readonly>
            </div>
            <div class="col-md-6">
                <label for="transacAmt" class="form-label">Transaction Amount ($)</label>
                <input class="form-control" type="text" name="transacAmt" id="transacAmt" value = 0>
            </div>
            <div>
                <button id="btnCrtNewCus" class="btn btn-outline-warning" type="submit">
                    <i class="fa fa-minus"></i>
                    Withdraw
                </button>
            </div>
        </fieldset>
    </form>

    <%@ include file="/layout/alert/error.jsp"%>
    <%@ include file="/layout/alert/warning.jsp"%>
    <%@ include file="/layout/alert/success.jsp"%>
</div>


<%@ include file="/layout/script.jsp" %>
</body>
</html>
