<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Customer Form</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/customerForm.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script>
	$(document).ready(function() {
		function getDate() {
			const today = new Date();
			$("#date").val(today.getFullYear() + '-' + ('0' + (today.getMonth() + 1)).slice(-2) + '-' + ('0' + today.getDate()).slice(-2));
		}
		console.log($("#date").val())
		if ($("#date").val() == null || $("#date").val() == "") {
			getDate();			
		}
		
	})
	
</script>
</head>
<body>
	<%@ include file="navBar.jsp" %>
	<div class="mainDiv customer">
		<h1 class="headerh1">CUSTOMER</h1>
		<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
			<form:form action="saveCustomer" method="POST" modelAttribute="customer">
				<table class="form customerForm">
					<tr hidden>
						<td>ID:</td>
						<td><form:input path="customerId" value="${customer.getCustomerId()}" /></td>
						<td><form:errors path="customerId" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Name:</td>
						<td><form:input path="customerName" value="${customer.getCustomerName()}" placeholder="name" /></td>
						<td><form:errors path="customerName" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Gender:</td>
						<td>
							<core:forEach items="${genders}" var="gender">
								<core:choose>
									<core:when test="${customerGender == gender}">
										<form:radiobutton path="customerGender" value="${gender}" label="${gender}" checked="true" />
									</core:when>
									<core:otherwise>
										<form:radiobutton path="customerGender" value="${gender}" label="${gender}" />
									</core:otherwise>
								</core:choose>
							</core:forEach>
						</td>
						<td><form:errors path="customerGender" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>DOB:</td>
						<td><form:input type="date" id="date" path="customerDOB" value="${customer.getCustomerDOB()}" /></td>
						<td><form:errors path="customerDOB" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Phone Number:</td>
						<td><form:input path="customerPhoneNum" value="${customer.getCustomerPhoneNum()}"  placeholder="123-456-7890" /></td>
						<td><form:errors path="customerPhoneNum" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Real Id:</td>
						<td><form:input path="customerRealId" value="${customer.getCustomerRealId()}"  placeholder="real id" /></td>
						<td><form:errors path="customerRealId" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td colspan="2"><h3 id="customerAddress">ADDRESS:</h3></td>
					</tr>
					<tr>
						<td>Address Line 1:</td>
						<td><form:input type="text" path="customerAddress.addressLine1" value="${customer.getCustomerAddress().getAddressLine1()}"  placeholder="address line 1" /></td>
						<td><form:errors path="customerAddress.addressLine1" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Address Line 2:</td>
						<td><form:input type="text" path="customerAddress.addressLine2" value="${customer.getCustomerAddress().getAddressLine2()}"  placeholder="address line 2" /></td>
						<td><form:errors path="customerAddress.addressLine2" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>City:</td>
						<td><form:input type="text" path="customerAddress.city" value="${customer.getCustomerAddress().getCity()}"  placeholder="city" /></td>
						<td><form:errors path="customerAddress.city" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>State:</td>
						<td><form:input type="text" path="customerAddress.state" value="${customer.getCustomerAddress().getState()}"  placeholder="state" /></td>
						<td><form:errors path="customerAddress.state" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Country:</td>
						<td><form:input type="text" path="customerAddress.country" value="${customer.getCustomerAddress().getCountry()}"  placeholder="country" /></td>
						<td><form:errors path="customerAddress.country" cssClass="errorMessage" /></td>
					</tr>
					<tr>
						<td>Zip code:</td>
						<td><form:input type="text" path="customerAddress.zipCode" value="${customer.getCustomerAddress().getZipCode()}"  placeholder="zip code" /></td>
						<td><form:errors path="customerAddress.zipCode" cssClass="errorMessage" /></td>
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

		<table class="dbList customerList">
			<tr>
				<th>CUSTOMER ID</th>
				<th>NAME</th>
				<th>GENDER</th>
				<th>DOB</th>
				<th>PHONE NUMBER</th>
				<th>ADDRESS</th>
				<th>REAL ID</th>
				<!-- <th>USER</th> -->
				<th>ACCOUNTS</th>
				<th>BALANCE</th>
				<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
					<th colspan="2">ACTIONS</th>
				</sec:authorize>
			</tr>
			<core:forEach items="${customers}" var="customer">
				<core:if test="${customer.getUser().getUsername() == loggedInUser || loggedInUserRoles.contains('Admin')}">
					<tr>
						<td>${customer.getCustomerId()}</td>
						<td>${customer.getCustomerName()}</td>
						<td>${customer.getCustomerGender()}</td>
						<td>${customer.getCustomerDOB()}</td>
						<td>${customer.getCustomerPhoneNum()}</td>
						<td>
							${customer.getCustomerAddress().getAddressLine1()}
							${customer.getCustomerAddress().getAddressLine2()}
							${customer.getCustomerAddress().getCity()},
							${customer.getCustomerAddress().getState()}
							${customer.getCustomerAddress().getCountry()}
							${customer.getCustomerAddress().getZipCode()}
						</td>
						<td>${customer.getCustomerRealId()}</td>
						<%-- <td>${customer.getUser().getUserId()} ${customer.getUser().getUsername()}</td> --%>
						<td>
						<core:forEach items="${customer.getCustomerAccounts()}" var="account">
							${account.accountId} ${account.accountType}<br>
						</core:forEach>
						</td>
						<td>
						<core:forEach items="${customer.getCustomerAccounts()}" var="account">
							$<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${account.accountBalance}"/><br>
						</core:forEach>
						</td>
						<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
							<td><a href="updateCustomer?customerId=${customer.getCustomerId()}"><button class="updateLink">UPDATE</button></a></td>
							<td><a href="deleteCustomer?customerId=${customer.getCustomerId()}"><button class="deleteLink">DELETE</button></a></td>
						</sec:authorize>
					</tr>
				</core:if>
			</core:forEach>
		</table>
	</div>

</body>
</html>