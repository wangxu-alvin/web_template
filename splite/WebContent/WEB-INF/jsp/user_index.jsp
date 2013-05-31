<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<html>
<head>
	<style type="text/css">
		td {
			font-size: 12px;
			font-family: Verdana, tahoma
		}
		
		.winUI {
			margin: 0 auto;
			border: solid 1px #d4d0c8;
		}
		
		.winUI tr td,.winUI thead td,tfoot td {
			border-left: 1px solid buttonhighlight;
			border-top: 1px solid buttonhighlight;
			border-bottom: 1px solid buttonshadow;
			border-right: 1px solid buttonshadow;
		}
		
		.winUI thead td,tfoot td {
			background-color: #ece9d8;
			cursor: pointer;
			padding: 4px;
			font-weight: bold;
		}
		
		.error {
			color: #ff0000;
			font-weight: bold;
		}
	</style>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
</head>
<body>
	<form:form modelAttribute="form" action="listUser.jspa">
		<br/>
		<table cellpadding="0" cellspacing="0" class="winUI" width="70%">
			<tr>
				<td><spring:message code="pagination.sample" />
				</td>
			</tr>
			<tr>
				<td>
					<form:select path="type">
						<form:option value="name"><spring:message code="customer.name" /></form:option>
						<form:option value="email"><spring:message code="customer.email" /></form:option>
					</form:select>
					<form:input path="condition"/>
					<input type="submit" value="<spring:message code="submit" />" />
				</td>
			</tr>
		</table>
	</form:form>
	<table cellpadding="4" cellspacing="0" border="1" class="winUI" width="70%">
		<thead>
			<td>ID</td>
			<td><spring:message code="customer.name" /></td>
			<td><spring:message code="customer.email" /></td>
			<td><spring:message code="last.modified" /></td>
		</thead>
		<tbody>
		<c:forEach items="${pageUsers}" var="entry" >
		<tr>
			<td>${entry.id}</td>
		  	<td>${entry.name}</td>
			<td>${entry.email}</td>
			<td>${entry.lastModified}</td>
		</tr>
		</c:forEach>
		<tr><td colspan="4" style="text-align: right">${footer}</td></tr>
		</tbody> 
	</table>
</body>
</html>