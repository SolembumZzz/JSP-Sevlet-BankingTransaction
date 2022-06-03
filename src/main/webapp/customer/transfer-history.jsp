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
    <title>Transfer History</title>
</head>
<body>
<div class="container">
    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h1>Transfer history</h1>
            </div>
            <%@ include file="/layout/nav-list-of-customers.jsp" %>
        </div>
    </div>

    <table class="table table-hover" id="HTTable">
        <thead>
        <tr>
            <th class="text-center">#</th>
            <th class="text-center">Sender ID</th>
            <th class="text-center">Sender Name</th>
            <th class="text-center">Recipient ID</th>
            <th class="text-center">Recipient Name</th>
            <th class="text-center">Transfer Amount ($)</th>
            <th class="text-center">Fees (%)</th>
            <th class="text-center">Fees Amount ($)</th>
        </tr>
        </thead>
        <tbody id="customerList">
        <c:forEach var="transfer" items="${transferHistory }">
            <tr>
                <td class="text-center">${transfer.getId()}</td>
                <td class="text-center">${transfer.getSenderId()}</td>
                <td>${transfer.getSenderName()}</td>
                <td class="text-center">${transfer.getRecipientId()}</td>
                <td>${transfer.getRecipientName()}</td>
                <td class="text-end">${transfer.getTransferAmt()}</td>
                <td class="text-center">${transfer.getFeeRate()}%</td>
                <td class="text-end">${transfer.getFeeAmount()}</td>
            </tr>
        </c:forEach>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="5"></td>
            <td class="text-center fw-bold">Total Fees Amount</td>
            <td class="text-end fw-bold" colspan="2" style="font-size: 24px">${totalFee}</td>
        </tr>
        </tfoot>
    </table>
</div>

<%@ include file="/layout/script.jsp" %>
</body>
</html>
