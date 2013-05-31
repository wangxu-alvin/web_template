<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="exception" /></title>
</head>
<body>
<h1>
<spring:message code="exception" /> : ${error_code}</h1>
<br/>
<h1>${error_message}</h1>
</body>
</html>