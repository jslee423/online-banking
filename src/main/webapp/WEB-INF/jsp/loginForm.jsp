<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Form</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<link href="${pageContext.request.contextPath}/css/loginForm.css" rel="stylesheet">
</head>
<body>
	<%@ include file="navBar.jsp" %>
	<div class="mainDiv login">
		<img id="logo-name" class="headerh1" src="${pageContext.request.contextPath}/img/logoipsum-name.svg" alt="temp logo"/>
		<h2 >Access You Account</h2>
		<core:if test="${message != null}">
			<p class="errorMessage">${message}</p>
		</core:if>
		<form action="login" method="POST">

			<input type="text" name="username" placeholder="username" /><br>

			<input type="password" name="password" placeholder="password" /><br>
			<input type="submit" value="LOGIN" />
		</form>
	</div>
</body>
</html>