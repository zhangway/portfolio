<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<body>
	<h1>${message}</h1>
	<p>
	<a href="<c:url value="/create_capability"/>">Create A Capability</a>
	</p>	
	
	
</body>
</html>