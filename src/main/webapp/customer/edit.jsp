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
    <title>Update customer information</title>
</head>
<body>
<div class="container">
    <div class="table-title mb-4">
        <div class="row">
            <div class="col-sm-5">
                <h3>Update customer information</h3>
            </div>
            <%@ include file="/layout/nav-list-of-customers.jsp" %>
        </div>
    </div>
    <form method="post">
        <fieldset class="row g-3">
            <div class="col-md-6">
                <label for="fullName" class="form-label">Full name</label>
                <input class="form-control" type="text" name="fullName" id="fullName"
                       value="${customer.getFullName()}" ${(error == 1) ? 'readonly' : ''}>
            </div>
            <div class="col-md-6">
                <label for="email" class="form-label">Email</label>
                <input class="form-control" type="text" name="email" id="email"
                       value="${customer.getEmail()}" ${(error == 1)? 'readonly' : ''}>
            </div>
            <div class="col-md-6">
                <label for="phone" class="form-label">Phone</label>
                <input class="form-control" type="tel" name="phone" id="phone"
                       value="${customer.getPhone()}" ${(error == 1) ? 'readonly' : ''}>
            </div>
            <div class="col-md-6">
                <label for="address" class="form-label">Address</label>
                <input class="form-control" type="text" name="address" id="address"
                       value="${customer.getAddress()}" ${(error == 1) ? 'readonly' : ''}>
            </div>
            <div>
                <button id="btnSaveChanges" class="btn btn-outline-secondary"
                        type="submit"}>
                    <i class="fa-solid fa-floppy-disk"></i>
                    Save changes
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
