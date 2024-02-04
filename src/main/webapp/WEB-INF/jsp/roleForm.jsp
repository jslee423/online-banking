<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Role Form</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/roleForm.css" rel="stylesheet">
</head>
<body>
	<%@ include file="navBar.jsp" %>
	<div class="mainDiv role">
		<h1 class="headerh1">ROLE</h1>
		<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
			<form:form action="saveRole" method="POST" modelAttribute="role">
				<table class="form roleForm">
					<tr hidden>
						<td>ID:</td>
						<td><form:input path="roleId" value="${role.getRoleId()}" /></td>
						<td><form:errors path="roleId" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Name:</td>
						<td><form:input path="name" value="${role.getName()}" placeholder="name" /></td>
						<td><form:errors path="name" cssClass="errorMessage" /></td>
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

		<table class="dbList roleList">
			<tr>
				<th>ROLE ID</th>
				<th>ROLE NAME</th>
				<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
					<th colspan="2">ACTIONS</th>
				</sec:authorize>
			</tr>
			<core:forEach items="${roles}" var="role">
				<tr>
					<td>${role.getRoleId()}</td>
					<td>${role.getName()}</td>
					<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
						<td><a href="updateRole?roleId=${role.getRoleId()}"><button class="updateLink">UPDATE</button></a></td>
						<td><a href="deleteRole?roleId=${role.getRoleId()}"><button class="deleteLink">DELETE</button></a></td>
					</sec:authorize>
				</tr>
			</core:forEach>
		</table>
	</div>

</body>
</html>