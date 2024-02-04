<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Online Banking</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/home.css" rel="stylesheet">
</head>
<body>
	<%-- <jsp:Include page="menu.jsp" /> --%>
	<%@ include file="navBar.jsp" %>
	<div class="mainDiv home <sec:authorize access="isAuthenticated()">homeAuthenticated</sec:authorize> <sec:authorize access="!isAuthenticated()">homeNotAuthenticated</sec:authorize>">
		<sec:authorize access="!isAuthenticated()">
			<div class="homeLogin">
				<form action="login" method="POST">
					<img id="logo-name" src="${pageContext.request.contextPath}/img/logoipsum-name.svg" alt="temp logo"/>
					<h2>Welcome</h2>
					<core:if test="${message != null}">
						<p class="errorMessage">${message}</p>
					</core:if>
					<input type="text" name="username" placeholder="username" /><br>
		
					<input type="password" name="password" placeholder="password" /><br>
					<input type="submit" value="LOGIN" />
					<p>I forgot my <a href="#">Username</a> or <a href="#">Password</a>.</p>
					<p>I want to <a href="#">Register</a> or <a href="#">Apply</a>.</p>
				</form>
				<div class="homeHeader">
					<h1>Manage your Logoipsum accounts and cards</h1>
					<ul>
						<li>View your activity & balance</li>
						<li>Make a payment or set up AutoPay</li>
						<li>Go paperless</li>
						<li>So much more!</li>
					</ul>
				</div>
			</div>
		</sec:authorize>
		<sec:authorize access="isAuthenticated()">
			<%-- <p class="loggedInUser">logged in as: <sec:authentication property="principal.username"/> <sec:authentication property="principal.authorities"/></p> --%>
			<h1 class="headerh1">Welcome back <sec:authentication property="principal.username"/></h1>
			
			<h2>My Accounts</h2>
			<div class="homeSection">
				<table class="dbList accountList">
					<tr>
						<th>ACCOUNT ID</th>
						<th>TYPE</th>
						<th>HOLDER</th>
						<th>BALANCE</th>
					</tr>
					<core:forEach items="${accounts}" var="account">
						<core:if test="${account.getAccountCustomer().getUser().getUsername() == loggedInUser}">
							<tr>
								<td>${account.getAccountId()}</td>
								<td>${account.getAccountType()}</td>
								<td>${account.getAccountHolder()}</td>
								<td>$<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${account.getAccountBalance()}"/></td>
							</tr>
						</core:if>
					</core:forEach>
				</table>
			</div>
			
			<h2>Recent Transactions</h2>
			<div class="homeSection">
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
		</sec:authorize>
	</div>

</body>
</html>