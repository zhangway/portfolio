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
<c:forEach var="outer" items="${capabilities}"  varStatus="seq1">
		
	<c:forEach var="inner" items="${outer.caveats}"  varStatus="seq2">
    	<tr>
			<td>${outer.name}</td>
	        <td>${outer.description}</td>
	        
	        <td>${inner.codeRef}</td>
	        <td>${inner.predicate}</td>
	        
   		</tr>
    </c:forEach>
</c:forEach>
</table>
	
	
</body>
</html>