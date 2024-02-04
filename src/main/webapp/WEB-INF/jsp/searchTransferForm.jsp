<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Search Transactions</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/searchTransferForm.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script>
	$(document).ready(function() {
		function getDate() {
			const today = new Date();
			const tenDaysBefore = new Date(new Date().getTime()-(10*24*60*60*1000));
			if ($("#fromDate").val() == null || $("#fromDate").val() == "") {
				$("#fromDate").val(tenDaysBefore.getFullYear() + '-' + ('0' + (tenDaysBefore.getMonth() + 1)).slice(-2) + '-' + ('0' + tenDaysBefore.getDate()).slice(-2));
			}
			if ($("#toDate").val() == null || $("#toDate").val() == "") {
				$("#toDate").val(today.getFullYear() + '-' + ('0' + (today.getMonth() + 1)).slice(-2) + '-' + ('0' + today.getDate()).slice(-2));
			}
		}
		getDate();
	})
</script>
</head>
<body>
<%@ include file="navBar.jsp" %>
<div class="mainDiv searchTransaction">
	<h1 class="headerh1">SEARCH TRANSACTIONS</h1>
	<form:form action="searchTransaction" method="GET">
		<p class="errorMessage searchError">${dateInputError}</p>
		<table class="form searchForm">
			<tr>
				<td>
					Transaction Type:<br><br>
					<select path="bankTransactionType" id="bankTransactionType" name="bankTransactionType">
						<option value="">--- SELECT ---</option>
						<core:forEach items="${transactionTypes}" var="type">
							<core:choose>
								<core:when test="${type == \"NEW_ACCOUNT\"}">
									<option value="${type}" label="${type}" disabled="true"/>
								</core:when>
								<core:otherwise>
									<core:if test="${selectedType == type}">
										<option value="${type}" label="${type}" selected="true" />
									</core:if>
									<core:if test="${selectedType != type}">
										<option value="${type}" label="${type}"/>
									</core:if>
								</core:otherwise>
							</core:choose>
						</core:forEach>
					</select>
							
				</td>

				<td>
					Account:<br><br>
					<select path="selectedAccount" id="selectedAccount" name="selectedAccount">
						<option value="">--- SELECT ---</option>
						<core:forEach items="${accounts}" var="account">
							<core:if test="${account.getAccountCustomer().getUser().getUsername() == loggedInUser}">
								<core:if test="${selectedAccount == account.accountId}">
									<option value="${account.accountId}" label="${account.accountId} ${account.accountType} ${account.accountBalance}" selected="true"/>
								</core:if>
								<core:if test="${selectedAccount != account.accountId}">
									<option value="${account.accountId}" label="${account.accountId} ${account.accountType} ${account.accountBalance}"/>
								</core:if>
							</core:if>
						</core:forEach>
					</select>
				</td>

				<td colspan="2">
					Dates:<br><br>
					<input type="date" id="fromDate" name="fromDate" value="${selectedFromDate}"/> to <input type="date" id="toDate" name="toDate" value="${selectedToDate}"/>
				</td>

				<td><input type="submit" value="SUBMIT" /></td>
			</tr>
		</table>
	</form:form>


	<table class="dbList transferList">
		<tr>
			<th>TRANSACTION #</th>
			<th>DATE</th>
			<th>FROM</th>
			<th>TO</th>
			<th>TYPE</th>
			<th>AMOUNT</th>
			<th>COMMENT</th>
		</tr>
		<core:forEach items="${userTransactions}" var="bankTransfer">
			<tr>
				<td>${bankTransfer.getBankTransactionId()}</td>
				<td>${bankTransfer.getBankTransactionDateTime()}</td>
				<core:choose>
					<core:when test="${bankTransfer.getBankTransactionFromAccount() != null}">
						<core:forEach items="${accounts}" var="account">
							<core:if test="${account.getAccountId() == bankTransfer.getBankTransactionFromAccount()}">
								<td>${account.getAccountId()} ${account.getAccountType()}</td>				
							</core:if>
						</core:forEach>
					</core:when>
					<core:otherwise>
						<td></td>
					</core:otherwise>
				</core:choose>
				<core:choose>
					<core:when test="${bankTransfer.getBankTransactionToAccount() != null}">
						<core:forEach items="${accounts}" var="account">
							<core:if test="${account.getAccountId() == bankTransfer.getBankTransactionToAccount()}">
								<td>${account.getAccountId()} ${account.getAccountType()}</td>				
							</core:if>
						</core:forEach>
					</core:when>
					<core:otherwise>
						<td></td>
					</core:otherwise>
				</core:choose>
				<td>${bankTransfer.getBankTransactionType()}</td>
				<td>$<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${bankTransfer.getBankTransactionAmount()}"/></td>
				<td>${bankTransfer.getComment()}</td>
			</tr>
		</core:forEach>
	</table>
</div>
</body>
</html>