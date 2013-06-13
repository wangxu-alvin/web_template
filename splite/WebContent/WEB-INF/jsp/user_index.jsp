<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tld/Pagination.tld" prefix="pagination" %>

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
	<script type="text/javascript">
		function doDelete(id, name) {
			if (confirm('<spring:message code="delete.confirmation"/> ' + name +'?')){
				window.location.href='deleteUser.jspa?id=' + id;
			}
		}
	</script>
</head>
<body>
	<form:form modelAttribute="form" action="prepare.jspa" name="form1">
		<br/>
		<table cellpadding="0" cellspacing="0" class="winUI" width="100%">
			<tr>
				<td><spring:message code="pagination.sample" />
				</td>
			</tr>
			<tr>
				<td>
					<form:select path="type">
						<form:option value="name"><spring:message code="user.name" /></form:option>
						<form:option value="email"><spring:message code="user.email" /></form:option>
					</form:select>
					<form:input path="condition"/>
					<input type="submit" value="<spring:message code="query" />" />
					<a href="prepareOperation.jspa"><spring:message code="user.add" /></a>
					<pagination:formHiddenElements/>
				</td>
			</tr>
		</table>
	</form:form>
	<table cellpadding="4" cellspacing="0" border="1" class="winUI" width="100%">
		<thead>
			<td><spring:message code="user.name" /></td>
			<td><spring:message code="user.email" /></td>
			<td><spring:message code="last.modified" /></td>
			<td><spring:message code="operation" /></td>
		</thead>
		<tbody>
		<c:forEach items="${pagination.rows}" var="entry" >
		<tr>
		  	<td>${entry.name}</td>
			<td>${entry.email}</td>
			<td>${entry.lastModified}</td>
			<td><a href="prepareOperation.jspa?id=${entry.id}"><spring:message code="update" /></a> | <a href="javascript:doDelete('${entry.id}', '${entry.name}')"><spring:message code="delete" /></a></td>
		</tr>
		</c:forEach>
		<tr>
			<td align="left">
				<pagination:capacitySelect pageCapacity="${pagination.pageCapacity}" />
			</td>
			<td colspan="3" align="right">
				<pagination:footer pageCapacity="${pagination.pageCapacity}" total="${pagination.total}" requestedPage="${pagination.currentPage}" />
				<pagination:submit formName="form1" />
			</td>
		</tr>
		</tbody> 
	</table>
</body>
</html>