<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<body>
	<table border="1">
<tr>
	<th>Name</th>
	<th>Description</th>
	<th>R SourceCode</th>
	<th>AccessPeriod</th>	
	
</tr>
<c:forEach items="${capabilities}" var="Capabilities">
    <tr>
        <td>${Capabilities.name}</td>
        <td>${Capabilities.description}</td>
        <td>${Capabilities.caveats[2].codeRef}</td>
        <td>${Capabilities.caveats[2].predicate}</td>
        
    </tr>
</c:forEach>
</table>
	
	
</body>
</html>