<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bank Transaction</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/bankTransferForm.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script>
	$(document).ready(function() {
		function getDate() {
			const today = new Date();
			$("#transferDate").val(today.getFullYear() + '-' + ('0' + (today.getMonth() + 1)).slice(-2) + '-' + ('0' + today.getDate()).slice(-2) + 'T' + today.getHours() + ':' + ('0' + today.getMinutes()).slice(-2));
		}
		getDate();
		
		$("#amount").val("20.00")
		
		$("#amount").on("blur", function() {
			let inputAmount = $(this).val()
			$(this).val(parseFloat(inputAmount).toFixed(2))
			
		})
		
		function disableAccountField() {
			//const type = $("#bankTransactionType").val()
			const type = $('input[name="bankTransactionType"]:checked').val()
			if (type == "DEPOSIT") {
				$("#labelDEPOSIT").css({'background-color': "green"})
				$("#labelDEPOSIT").siblings().css({'background-color': "lightgrey"})
				$("#bankTransactionFromAccount").attr("disabled", "true")
				$("#bankTransactionToAccount").removeAttr("disabled")
			} else if (type == "WITHDRAWAL") {
				$("#labelWITHDRAWAL").css({'background-color': "orangered"})
				$("#labelWITHDRAWAL").siblings().css({'background-color': "lightgrey"})
				$("#bankTransactionToAccount").attr("disabled", "true")
				$("#bankTransactionFromAccount").removeAttr("disabled")
			} else if (type == "TRANSFER") {
				$("#labelTRANSFER").css({'background-color': "#085D7E"})
				$("#labelTRANSFER").siblings().css({'background-color': "lightgrey"})
				$("#bankTransactionToAccount").removeAttr("disabled")
				$("#bankTransactionFromAccount").removeAttr("disabled")
			}
		}
		disableAccountField();
		
		$(".bankTransactionType").on("change", function() {
			const type = $(this).val()
			if (type == "DEPOSIT") {
				$("#labelDEPOSIT").css({'background-color': "green"})
				$("#labelDEPOSIT").siblings().css({'background-color': "lightgrey"})
				$("#bankTransactionFromAccount").attr("disabled", "true")
				$("#bankTransactionToAccount").removeAttr("disabled")
			} else if (type == "WITHDRAWAL") {
				$("#labelWITHDRAWAL").css({'background-color': "orangered"})
				$("#labelWITHDRAWAL").siblings().css({'background-color': "lightgrey"})
				$("#bankTransactionToAccount").attr("disabled", "true")
				$("#bankTransactionFromAccount").removeAttr("disabled")
			} else if (type == "TRANSFER") {
				$("#labelTRANSFER").css({'background-color': "#085D7E"})
				$("#labelTRANSFER").siblings().css({'background-color': "lightgrey"})
				$("#bankTransactionToAccount").removeAttr("disabled")
				$("#bankTransactionFromAccount").removeAttr("disabled")
			}
		})
		
	})
	
</script>
</head>
<body>
	<%@ include file="navBar.jsp" %>
	<div class="mainDiv bankTransfer">
		<h1 class="headerh1">BANK TRANSACTION</h1>
		<form:form action="saveBankTransfer" method="POST" modelAttribute="bankTransaction">
			<table class="form bankTransferForm">
				<tr>
					<td colspan="2"><form:input type="datetime-local" id="transferDate" path="bankTransactionDateTime" value="${bankTransaction.getBankTransactionDateTime()}" readonly="true" /></td>
					<td><form:errors path="bankTransactionDateTime" cssClass="errorMessage" /></td>
				</tr>
				<tr>
					<td colspan="2"><form:input type="number" step=".01" min="1.00" id="amount" path="bankTransactionAmount" value="${bankTransaction.getBankTransactionAmount()}" placeholder="$00.00" /></td>
					<td><form:errors path="bankTransactionAmount" cssClass="errorMessage" /></td>
				</tr>
				<tr>
					<td>
						<core:forEach items="${transactionTypes}" var="type">
							<core:if test="${type != \"NEW_ACCOUNT\"}">
								<form:label path="bankTransactionType" for="${type}" class="typeLabel" id="label${type}">
									<form:radiobutton path="bankTransactionType" id="${type}" class="bankTransactionType" value="${type}"  />
									${type}
								</form:label>
							</core:if>
						</core:forEach>
					</td>
					<!-- 
					<td>
						<form:select path="bankTransactionType" id="bankTransactionType">
							<form:option value="">--- SELECT ---</form:option>
							<core:forEach items="${transactionTypes}" var="type">
								<core:choose>
									<core:when test="${type == \"NEW_ACCOUNT\"}">
										<form:option value="${type}" label="${type}" disabled="true"/>
									</core:when>
									<core:otherwise>
										<form:option value="${type}" label="${type}"/>
									</core:otherwise>
								</core:choose>
							</core:forEach>
						</form:select>
								
					</td> -->
				</tr>
				<tr>
					<td><form:errors path="bankTransactionType" cssClass="errorMessage" /></td>
				</tr>
				<tr>
					<td>
						<form:select path="bankTransactionFromAccount" id="bankTransactionFromAccount">
							<form:option value="">--- SELECT ---</form:option>
							<core:forEach items="${accounts}" var="account">
								<core:if test="${account.getAccountCustomer().getUser().getUsername() == loggedInUser}">
									<form:option value="${account.accountId}" label="${account.accountId} ${account.accountType} ${account.accountBalance}"/>
								</core:if>
							</core:forEach>
						</form:select>
						TO
						<form:select path="bankTransactionToAccount" id="bankTransactionToAccount">
							<form:option value="">--- SELECT ---</form:option>
							<core:forEach items="${accounts}" var="account">
								<core:if test="${account.getAccountCustomer().getUser().getUsername() == loggedInUser}">
									<form:option value="${account.accountId}" label="${account.accountId } ${account.accountType} ${account.accountBalance}"/>
								</core:if>
							</core:forEach>
						</form:select>
					</td>
				</tr>
				<tr>
					<td>
						<form:errors path="bankTransactionFromAccount" cssClass="errorMessage" />
						<span>&nbsp&nbsp&nbsp&nbsp</span>
						<form:errors path="bankTransactionToAccount" cssClass="errorMessage" />
					</td>
				</tr>
				<tr>
					<td>Comment(optional):</td>
				</tr>
				<tr>
					<td><form:input path="comment" value="${customer.getComment()}" placeholder="comment" /></td>
					<td><form:errors path="comment" cssClass="errorMessage" /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="SUBMIT" /></td>
				</tr>
			</table>
		</form:form>

		<!-- 
		<sec:authorize access="hasAuthority('Admin') || hasAuthority('DBA')">
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
				<core:forEach items="${bankTransfers}" var="bankTransfer">
					<%-- <core:if test="${userAccounts.contains(bankTransfer.getBankTransactionFromAccount()) || userAccounts.contains(bankTransfer.getBankTransactionToAccount())}"> --%>
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
					<%-- </core:if> --%>
				</core:forEach>
			</table>
		</sec:authorize>
	</div>
	 -->
</body>
</html>