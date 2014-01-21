<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>RunKeeper</title>
</head>
<body>
<p>
	RunKeeper Dashboard one-time code ${param.code}
</p>

<form action="gettoken" method="post">
    <input type="text" name="code">
    
    <input type="submit" value="Get Token">
    <span class="error">${error}</span>
</form>

</body>
</html>
