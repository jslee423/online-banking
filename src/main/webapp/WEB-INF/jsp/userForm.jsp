<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Form</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/userForm.css" rel="stylesheet">
</head>
<body>
	<%@ include file="navBar.jsp" %>
	<div class="mainDiv user">
		<h1 class="headerh1">USER</h1>
		<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
			<form:form action="saveUser" method="POST" modelAttribute="user">
				<table class="form userForm">
					<tr>
						<td>Customer Id:</td>
						<td><form:input path="userId" value="${user.getUserId()}" placeholder="customer id" /></td>
						<td><form:errors path="userId" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Username:</td>
						<td><form:input path="username" value="${user.getUsername()}" placeholder="username" /></td>
						<td><form:errors path="username" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><form:input path="password" value="${user.getPassword()}" placeholder="password" /></td>
						<td><form:errors path="password" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Email:</td>
						<td><form:input path="email" value="${user.getEmail()}" placeholder="email" /></td>
						<td><form:errors path="email" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td colspan="2"><h3 id="userRoleHeader">ROLES:</h3></td>
					</tr>
					<tr id="rowCheckboxes">
						<td colspan="2">
							<core:forEach items="${roles}" var="role" >
								<core:if test="${userRoles.contains(role)}">
									<form:checkbox path="roles" label="${role.name}" value="${role.roleId}" checked="true"/>
								</core:if>
								<core:if test="${!userRoles.contains(role)}">
									<form:checkbox path="roles" label="${role.name}" value="${role.roleId}" />
								</core:if>
							</core:forEach>
						</td>
						<td><form:errors path="roles" cssClass="errorMessage" /></td>
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

		<table class="dbList userList">
			<tr>
				<th>USER ID</th>
				<th>USER NAME</th>
				<th>EMAIL</th>
				<th>ROLES</th>
				<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
					<th colspan="2">ACTIONS</th>
				</sec:authorize>
			</tr>
			<core:forEach items="${users}" var="user">
				<core:if test="${user.getUsername() == loggedInUser || loggedInUserRoles.contains('Admin')}">
					<tr>
						<td>${user.getUserId()}</td>
						<td>${user.getUsername()}</td>
						<td>${user.getEmail()}</td>
						<td>
							<core:forEach items="${user.getRoles()}" var="role">
								${role.getName()}<br>
							</core:forEach>
						</td>
						<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
							<td><a href="updateUser?userId=${user.getUserId()}"><button class="updateLink">UPDATE</button></a></td>
							<td><a href="deleteUser?userId=${user.getUserId()}"><button class="deleteLink">DELETE</button></a></td>
						</sec:authorize>
					</tr>
				</core:if>
			</core:forEach>
		</table>
	</div>
</body>
</html>