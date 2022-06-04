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
    <title>Transfer</title>
</head>
<body>
<div class="container">
    <div class="table-title mb-4">
        <div class="row">
            <div class="col-sm-5">
                <h1>Transfer</h1>
            </div>
            <%@ include file="/layout/nav-list-of-customers.jsp" %>
        </div>
    </div>

    <form id="frmTransfer" method="post">
        <fieldset class="row g-3">
            <div class="col-sm-6 col-lg-3">
                <label class="form-label">Sender ID</label>
                <input class="form-control" type="text" name="senderID" id="senderID"
                       value="${customer.getId()}" readonly>
            </div>
            <div class="col-sm-6 col-lg-3">
                <label class="form-label">Sender Name</label>
                <input class="form-control" type="text" name="senderName" id="senderName"
                       value="${customer.getFullName()}" readonly>
            </div>
            <div class="col-sm-6 col-lg-3">
                <label class="form-label">Email</label>
                <input class="form-control" type="email" name="email" id="email"
                       value="${customer.getEmail()}" readonly>
            </div>
            <div class="col-sm-6 col-lg-3">
                <label class="form-label">Sender balance</label>
                <input class="form-control" type="text" name="senderBalance" id="senderBalance"
                       value="${customer.getBalance()}" readonly>
            </div>
            <div class="col-sm-6 col-lg-3">
                <label for="recipientId" class="form-label">Recipient</label>
                <select class="form-select" name="recipientId" id="recipientId" required>
                    <option selected disabled>Choose recipient</option>
                    <c:forEach var="recipient" items="${recipients}">
                        <option value="${recipient.getId()}"
                            ${(currentRecipient != null && recipient.getId() eq currentRecipient) ? 'selected' : ''}>
                            (${recipient.getId()}) ${recipient.getFullName()}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-sm-6 col-lg-3">
                <label for="transferAmt" class="form-label">Transfer Amount ($)</label>
                <input class="form-control" type="text" name="transferAmt" id="transferAmt" value="0">
            </div>
            <div class="col-sm-6 col-lg-3">
                <label class="form-label">Fees (%)</label>
                <input class="form-control" type="text" name="fees" id="fees"
                       value="${fees}" readonly>
            </div>
            <div class="col-sm-6 col-lg-3">
                <label class="form-label">Total amount of transaction ($)</label>
                <input class="form-control" type="text" name="totalTransaction" id="totalTransaction"
                       value='0' readonly>
            </div>
            <div class="col-md-12">
                <button id="btnCrtNewCus" class="btn btn-outline-primary" type="submit">
                    <i class="fa fa-exchange"></i>
                    Transfer
                </button>
            </div>
        </fieldset>
    </form>

    <%@ include file="/layout/alert/error.jsp" %>
    <%@ include file="/layout/alert/warning.jsp" %>
    <%@ include file="/layout/alert/success.jsp" %>
</div>

<%@ include file="/layout/script.jsp" %>
<script>
    document.addEventListener("input", function () {
        let transferAmt = parseInt(document.getElementById("transferAmt").value);
        let totalTransaction =  transferAmt*((100 + parseInt(document.getElementById("fees").value))/100);
        document.getElementById("totalTransaction").value = Math.round(totalTransaction);
    });
</script>
</body>
</html>
