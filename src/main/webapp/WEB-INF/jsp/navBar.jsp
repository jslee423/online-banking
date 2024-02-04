<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="${pageContext.request.contextPath}/css/navBar.css" rel="stylesheet">
</head>
<body>
<!-- 
	<table class="nav">
		<tr>
			<td><a href="${pageContext.request.contextPath}/"><img src="${pageContext.request.contextPath}/img/logoipsum.svg" alt="temp logo"/></a></td>
			<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
				<td><a href="${pageContext.request.contextPath}/branchForm">BRANCH FORM</a></td>
				<td><a href="${pageContext.request.contextPath}/roleForm">ROLE FORM</a></td>
			</sec:authorize>
			<td><a href="${pageContext.request.contextPath}/userForm">USER FORM</a></td>
			<td><a href="${pageContext.request.contextPath}/customerForm">CUSTOMER FORM</a></td>
			<td><a href="${pageContext.request.contextPath}/accountForm">ACCOUNT FORM</a></td>
			<td><a href="${pageContext.request.contextPath}/bankTransferForm">BANK TRANSACTION</a></td>
			<td><a href="${pageContext.request.contextPath}/searchTransferForm">SEARCH</a></td>
			<sec:authorize access="!isAuthenticated()">
				<td><a href="${pageContext.request.contextPath}/login">LOGIN</a></td>
			</sec:authorize>
			<sec:authorize access="isAuthenticated()">
				<td><a href="${pageContext.request.contextPath}/logout">LOGOUT</a></td>
			</sec:authorize>
		</tr>
	</table>
	 -->
	<nav class="nav">
		<div class="logo">
			<a href="${pageContext.request.contextPath}/"><img src="${pageContext.request.contextPath}/img/logoipsum.svg" alt="temp logo"/></a>
			<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
				<h4 id="adminBadge">ADMIN/DBA</h4>
			</sec:authorize>
		</div>
		<ul>
			<sec:authorize access="isAuthenticated()">
				<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
					<li><a class="${activeBranch}" href="${pageContext.request.contextPath}/branchForm">BRANCHES</a></li>
					<li><a class="${activeRole}" href="${pageContext.request.contextPath}/roleForm">ROLES</a></li>
					<li><a class="${activeUser}" href="${pageContext.request.contextPath}/userForm">USERS</a></li>
					<li><a class="${activeCustomer}" href="${pageContext.request.contextPath}/customerForm">CUSTOMERS</a></li>
				</sec:authorize>
				<li><a class="${activeTransaction}" href="${pageContext.request.contextPath}/bankTransferForm">MAKE TRANSACTION</a></li>
				<li><a class="${activeSearch}" href="${pageContext.request.contextPath}/searchTransferForm">SEARCH</a></li>
				<li><a class="${activeAccount}" href="${pageContext.request.contextPath}/accountForm">ACCOUNTS</a></li>
			</sec:authorize>
			<sec:authorize access="!isAuthenticated()">
				<li><a href="${pageContext.request.contextPath}/login">LOGIN</a></li>
			</sec:authorize>
			<sec:authorize access="isAuthenticated()">
				<li><a href="${pageContext.request.contextPath}/logout">LOGOUT</a></li>
			</sec:authorize>
		</ul>
	</nav>
</body>
</html>