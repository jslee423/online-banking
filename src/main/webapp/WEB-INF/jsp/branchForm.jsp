<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Branch Form</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/branchForm.css" rel="stylesheet">
</head>
<body>
	<%@ include file="navBar.jsp" %>
	<div class="mainDiv branch">
		<!--  
		<table class="nav">
			<tr>
				<td><a href="${pageContext.request.contextPath}/">HOME</a></td><td>|</td>
				<td><a href="${pageContext.request.contextPath}/branchForm">BRANCH FORM</a></td><td>|</td>
				<td><a href="${pageContext.request.contextPath}/roleForm">ROLE FORM</a></td><td>|</td>
				<td><a href="${pageContext.request.contextPath}/userForm">USER FORM</a></td><td>|</td>
				<td><a href="${pageContext.request.contextPath}/customerForm">CUSTOMER FORM</a></td><td>|</td>
				<td><a href="${pageContext.request.contextPath}/accountForm">ACCOUNT FORM</a></td><td>|</td>
				<td><a href="${pageContext.request.contextPath}/bankTransferForm">BANK TRANSACTION</a></td><td>|</td>
				<td><a href="${pageContext.request.contextPath}/searchTransferForm">SEARCH</a></td>
				<sec:authorize access="isAuthenticated()">
					<td>|</td>
					<td><a href="${pageContext.request.contextPath}/logout">LOGOUT</a></td>
				</sec:authorize>
			</tr>
		</table>
		<sec:authorize access="isAuthenticated()">
			<p class="loggedInUser">logged in as: <sec:authentication property="principal.username"/> <sec:authentication property="principal.authorities"/></p>
		</sec:authorize>
		-->
		<h1 class="headerh1">BRANCH</h1>
		<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
			<form:form action="saveBranch" method="POST" modelAttribute="branch">
				<table class="form branchForm">
					<tr hidden>
						<td>ID:</td>
						<td><form:input path="branchId" value="${branch.getBranchId()}" /></td>
						<td><form:errors path="branchId" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Name:</td>
						<td><form:input path="branchName" value="${branch.getBranchName()}" placeholder="name" /></td>
						<td><form:errors path="branchName" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td colspan="2"><h3 id="branchAddress">Address:</h3></td>
					</tr>
					<tr>
						<td>Address Line 1:</td>
						<td><form:input type="text" path="branchAddress.addressLine1" value="${branch.getBranchAddress().getAddressLine1()}"  placeholder="address line 1" /></td>
						<td><form:errors path="branchAddress.addressLine1" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Address Line 2:</td>
						<td><form:input type="text" path="branchAddress.addressLine2" value="${branch.getBranchAddress().getAddressLine2()}"  placeholder="address line 2" /></td>
					</tr>
					<tr>
						<td>City:</td>
						<td><form:input type="text" path="branchAddress.city" value="${branch.getBranchAddress().getCity()}" placeholder="city" /></td>
						<td><form:errors path="branchAddress.city" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>State:</td>
						<td><form:input type="text" path="branchAddress.state" value="${branch.getBranchAddress().getState()}" placeholder="state" /></td>
						<td><form:errors path="branchAddress.state" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Country:</td>
						<td><form:input type="text" path="branchAddress.country" value="${branch.getBranchAddress().getCountry()}" placeholder="country" /></td>
						<td><form:errors path="branchAddress.country" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Zip code:</td>
						<td><form:input type="text" path="branchAddress.zipCode" value="${branch.getBranchAddress().getZipCode()}" placeholder="zip code" /></td>
						<td><form:errors path="branchAddress.zipCode" cssClass="errorMessage" /></td>
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

		<table class="dbList branchList">
			<tr>
				<th>BRANCH ID</th>
				<th>BRANCH NAME</th>
				<th>BRANCH ADDRESS</th>
				<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
					<th colspan="2">ACTIONS</th>
				</sec:authorize>
			</tr>
			<core:forEach items="${branches}" var="branch">
				<tr>
					<td>${branch.getBranchId()}</td>
					<td>${branch.getBranchName()}</td>
					<td>
						${branch.getBranchAddress().getAddressLine1()}
						${branch.getBranchAddress().getAddressLine2()}
						${branch.getBranchAddress().getCity()},
						${branch.getBranchAddress().getState()}
						${branch.getBranchAddress().getCountry()}
						${branch.getBranchAddress().getZipCode()}
					</td>
					<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
						<td><a href="updateBranch?branchId=${branch.getBranchId()}"><button class="updateLink">UPDATE</button></a></td>
						<td><a href="deleteBranch?branchId=${branch.getBranchId()}"><button class="deleteLink">DELETE</button></a></td>
					</sec:authorize>
				</tr>
			</core:forEach>
		</table>
	</div>
</body>
</html>