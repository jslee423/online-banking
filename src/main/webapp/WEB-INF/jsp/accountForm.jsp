<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Account Form</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/accountForm.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script>
	$(document).ready(function() {
		function getDate() {
			const today = new Date();
			$("#openDate").val(today.getFullYear() + '-' + ('0' + (today.getMonth() + 1)).slice(-2) + '-' + ('0' + today.getDate()).slice(-2));
		}
		getDate();
		
	})
	
</script>
</head>
<body>
	<%@ include file="navBar.jsp" %>
	<div class="mainDiv account">
		<h1 class="headerh1">ACCOUNT</h1>
		<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
			<form:form action="saveAccount" method="POST" modelAttribute="account">
				<table class="form accountForm">
					<tr>
						<td>Open date:</td>
						<td><form:input type="date" id="openDate" path="accountOpenDate" value="${account.getAccountOpenDate()}" readonly="true" /></td>
						<td><form:errors path="accountOpenDate" cssClass="errorMessage" /></td>
					</tr>
					<tr hidden>
						<td>ACCOUNT ID:</td>
						<td><form:input path="accountId" value="${account.getAccountId()}" /></td>
						<td><form:errors path="accountId" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Type:</td>
						<td>
							<core:forEach items="${accountTypes}" var="accountType">
								<core:choose>
									<core:when test="${currentAccountType == accountType}">
										<form:radiobutton path="accountType" value="${accountType}" label="${accountType}" checked="true" />
									</core:when>
									<core:otherwise>
										<form:radiobutton path="accountType" value="${accountType}" label="${accountType}" />
									</core:otherwise>
								</core:choose>
							</core:forEach>
						</td>
						<td><form:errors path="accountType" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Account Holder:</td>
						<td><form:input path="accountHolder" value="${account.getAccountHolder()}" placeholder="account holder" /></td>
						<td><form:errors path="accountHolder" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Balance:</td>
						<td><form:input path="accountBalance" value="${account.getAccountBalance()}" /></td>
						<td><form:errors path="accountBalance" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Branch:</td>
						<td>
							<core:forEach items="${branches}" var="branch">
								<core:choose>
										<core:when test="${accountBranch == branch}">
											<form:radiobutton path="accountBranch" value="${branch}" label="${branch.branchName}" checked="true" />
										</core:when>
										<core:otherwise>
											<form:radiobutton path="accountBranch" value="${branch}" label="${branch.branchName}" />
										</core:otherwise>
									</core:choose>
							</core:forEach>
						</td>
						<td><form:errors path="accountBranch" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Customer:</td>
						<td>
							<form:select path="accountCustomer">
								<form:option value="">--- SELECT ---</form:option>
								<core:forEach items="${customers}" var="customer">
									<core:choose>
										<core:when test="${account.getAccountCustomer() == customer}">
											<form:option value="${customer}" label="${customer.customerId} ${customer.customerName}" selected="true" />
										</core:when>
										<core:otherwise>
											<form:option value="${customer}" label="${customer.customerId} ${customer.customerName}" />
										</core:otherwise>
									</core:choose>
								</core:forEach>
							</form:select>
						</td>
						<td><form:errors path="accountCustomer" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<core:if test="${updateBtn}">
							<td colspan="2"><input type="submit" value="UPDATE" id="updateBtn" /></td>
						</core:if>
						<core:if test="${!updateBtn}">
							<td colspan="2"><input type="submit" value="SUBMIT" /></td>
						</core:if>
					</tr>
				</table>
			</form:form>
		</sec:authorize>

		<table class="dbList accountList">
			<tr>
				<th>ACCOUNT ID</th>
				<th>TYPE</th>
				<th>OPEN DATE</th>
				<th>HOLDER</th>
				<th>BALANCE</th>
				<th>BRANCH</th>
				<th>CUSTOMER</th>
				<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
					<th colspan="2">ACTIONS</th>
				</sec:authorize>
			</tr>
			<core:forEach items="${accounts}" var="account">
				<core:if test="${account.getAccountCustomer().getUser().getUsername() == loggedInUser || loggedInUserRoles.contains('Admin')}">
					<tr>
						<td>${account.getAccountId()}</td>
						<td>${account.getAccountType()}</td>
						<td>${account.getAccountOpenDate()}</td>
						<td>${account.getAccountHolder()}</td>
						<td>$<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${account.getAccountBalance()}"/></td>
						<td>${account.getAccountBranch().getBranchName()}</td>
						<td>${account.getAccountCustomer().getCustomerName()}</td>
						<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
							<td><a href="updateAccount?accountId=${account.getAccountId()}"><button class="updateLink">UPDATE</button></a></td>
							<td><a href="deleteAccount?accountId=${account.getAccountId()}"><button class="deleteLink">DELETE</button></a></td>
						</sec:authorize>
					</tr>
				</core:if>
			</core:forEach>
		</table>
	</div>
</body>
</html>