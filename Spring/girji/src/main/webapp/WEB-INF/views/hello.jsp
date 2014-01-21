<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<body>
	<h1>Message : ${message}</h1>	
	
	<p>
	<a href="<c:url value="/runkeeper"/>">Connect to RunKeeper</a>
	</p>
</body>
</html>