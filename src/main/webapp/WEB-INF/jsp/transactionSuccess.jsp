<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Transaction Success</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/transactionSuccess.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<%@ include file="navBar.jsp" %>
<div class="mainDiv transactionSuccess">
	<h1 class="headerh1">${savedTransaction.getBankTransactionType()} SUCCESSFUL</h1>
	<a href="bankTransferForm">Make another transaction</a>
	<br>
	<a href="accountForm">View all accounts</a>
	<h3>${savedTransaction.getBankTransactionDateTime()}</h3>
	<h2>Transaction id: ${savedTransaction.getBankTransactionId()}</h2>
	<core:if test="${savedTransaction.getBankTransactionType() != 'DEPOSIT'}">
		<h2>From: ${savedTransaction.getBankTransactionFromAccount()} ${savedFrom}</h2>
	</core:if>
	<core:if test="${savedTransaction.getBankTransactionType() != 'WITHDRAWAL'}">
		<h2>To: ${savedTransaction.getBankTransactionToAccount()} ${savedFrom}</h2>
	</core:if>
	<h2>
		Amount: $<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${savedTransaction.getBankTransactionAmount()}"/>
	</h2>
</div>
</body>
</html>