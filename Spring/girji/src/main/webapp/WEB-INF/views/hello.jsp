<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<body>
	<h1>Message : ${message}</h1>	
	
	<p>
	<a href="<c:url value="/runkeeper"/>">Connect to RunKeeper</a>
	</p>
	<p>
	<a href="<c:url value="/mycapabilities"/>">My Capabilities</a>
	</p>
	<p>
	<a href="<c:url value="/execute"/>">Execute</a>
	</p>
	<p>
	<a href="<c:url value="/test"/>">test</a>
	</p>
	
	<p>
	<a href="<c:url value="/dumbtest"/>">Dumb Test</a>
	</p>
	
	<p>
	<a href="<c:url value="/register"/>">Register a service</a>
	</p>
	

	<p>
	<a href="<c:url value="/createcap"/>">Create A Capability</a>
	</p>
	
	<p>
	<a href="<c:url value="/delegatecap"/>">Delegate A Capability</a>
	</p>

</body>
</html>