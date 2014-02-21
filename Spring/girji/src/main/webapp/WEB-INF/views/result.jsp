<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Execution Result</title>
</head>
<body>

<p>${filePath}</p>

<c:url value="download.htm" var="displayURL">
  <c:param name="file"   value="${filePath}" />
 
</c:url>
<a href='<c:out value="${displayURL}" />'>${filePath}</a>

</body>
</html>
