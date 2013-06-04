<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
<style>
.error {
	color: #ff0000;
}
 
.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>
</head>
 
<body>
	<form:form method="POST" modelAttribute="user" action="${user.id == 0 ? 'addUser.jspa' : 'updateUser.jspa'}">
		<form:errors path="*" cssClass="errorblock" element="div" />
		<table>
			<tr>
				<td><spring:message code="user.name" text="1" /></td>
				<td><form:input path="name"/></td>
				<td><form:errors path="name" cssClass="error" /></td>
			</tr>
			<tr>
				<td><spring:message code="user.password" text="2" /></td>
				<td><form:input path="password" /></td>
				<td><form:errors path="password" cssClass="error" /></td>
			</tr>
			<tr>
				<td><spring:message code="user.email" text="3" /></td>
				<td><form:input path="email" /></td>
				<td><form:errors path="email" cssClass="error" /></td>
			</tr>
			<tr>
				<td colspan="3"><input type="submit" value="<spring:message code="submit" />" /><input type="button" value="<spring:message code="return" />" onclick="javascript:window.location.href='listUser.jspa'"/></td>
			</tr>
		</table>
		<form:hidden path="id"/>
		<form:hidden path="lastModified"/>
	</form:form>
 
</body>
</html>