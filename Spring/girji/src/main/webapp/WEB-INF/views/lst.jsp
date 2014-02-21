<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<body>
	<c:forEach var="outer" items="${capabilities}"  varStatus="seq1">
		<h3>seq1: ${seq1.count}</h3>  
		<h3>${outer.name}</h3>
		<h3>${outer.description}</h3>
		 <c:forEach var="inner" items="${outer.caveats}" varStatus="seq2">
    		seq2: ${seq2.count} - ${inner.codeRef}  <br/>
  		</c:forEach>
    
	</c:forEach>
	
	
</body>
</html>