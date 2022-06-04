<%--
  Created by IntelliJ IDEA.
  User: XV
  Date: 01-Jun-22
  Time: 10:43 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <%@ include file="/layout/head.jsp" %>
    <title>Add customer</title>
</head>
<body>
<div class="container">
    <div class="table-title mb-4">
        <div class="row">
            <div class="col-sm-5">
                <h1>Suspend</h1>
            </div>
            <%@ include file="/layout/nav-list-of-customers.jsp" %>
        </div>
    </div>
    <form method="post" action="${pageContext.request.contextPath}/customers?action=suspend&id=${customer.getId()}">
        <fieldset class="row g-3">
            <div class="col-md-6">
                <label for="fullName" class="form-label">Full name</label>
                <input class="form-control" type="text" name="fullName" id="fullName"
                       value="${customer.getFullName()}" readonly>
            </div>
            <div class="col-md-6">
                <label for="email" class="form-label">Email</label>
                <input class="form-control" type="text" name="email" id="email"
                       value="${customer.getEmail()}" readonly>
            </div>
            <div class="col-md-6">
                <label for="phone" class="form-label">Phone</label>
                <input class="form-control" type="tel" name="phone" id="phone"
                       value="${customer.getPhone()}" readonly>
            </div>
            <div class="col-md-6">
                <label for="address" class="form-label">Address</label>
                <input class="form-control" type="text" name="address" id="address"
                       value="${customer.getAddress()}" readonly>
            </div>
            <div>
                <button id="btnCrtNewCus" class="btn btn-outline-danger" type="submit">
                    <i class="fa fa-ban"></i>
                    Suspend customer
                </button>
            </div>
        </fieldset>
    </form>

    <%@ include file="/layout/alert/error.jsp" %>
    <%@ include file="/layout/alert/warning.jsp" %>
    <%@ include file="/layout/alert/success.jsp" %>
</div>

<%@ include file="/layout/script.jsp" %>
</body>
</html>
