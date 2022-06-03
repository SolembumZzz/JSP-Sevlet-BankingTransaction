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
    <%@ include file="/layout/head.jsp"%>
    <title>Create Customer</title>
</head>
<body>
<div class="container">
    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h1>Create customer</h1>
            </div>
            <%@ include file="/layout/nav-list-of-customers.jsp"%>
        </div>
    </div>
    <div>
        <form method="post">
            <div class="row mt-3">
                <div class="col-md-6 mb-3">
                    <label for="fullName" class="form-label">Full Name</label>
                    <input type="text" class="form-control" name="fullName" id="fullName">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" name="email" id="email">
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="phone" class="form-label">Phone</label>
                    <input type="text" class="form-control" name="phone" id="phone">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="address" class="form-label">Address</label>
                    <input type="text" class="form-control" name="address" id="address">
                </div>
            </div>
            <button type="submit" class="btn btn-outline-primary">
                <i class="fa fa-plus"></i>
                Add Customer</button>
        </form>
    </div>
    <%@ include file="/layout/alert/error.jsp"%>
    <%@ include file="/layout/alert/warning.jsp"%>
    <%@ include file="/layout/alert/success.jsp"%>
</div>

<%@ include file="/layout/script.jsp" %>
</body>
</html>
