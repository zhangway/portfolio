<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Home</title>
</head>
<body>
<P>  The time on the server is ${serverTime}. </P>
<h3>Message : ${message}</h3>	
	<h3>Username : ${username}</h3>	
	
	<a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
</body>
</html>


