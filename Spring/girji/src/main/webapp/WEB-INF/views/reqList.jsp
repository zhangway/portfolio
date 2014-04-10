<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>



</head>
<body>
	<h1>Choose an analytical service</h1>
	
	<form action="choose.htm">

		<table id="operation" border="1">
			<caption>CRO List</caption>
			<tr>
				<th></th>
				<th>name</th>
				<th>description</th>
				
			</tr>

			<c:forEach items="${reqView}" var="view">
				<tr>
					<td><input type="radio" name="radio" value="${view.name}"/></td>
					<td>${view.name}</td>
					<td>${view.description}</td>
					
				</tr>
			</c:forEach>

			
			
		</table>
		<input type="submit" value="Submit">
</form>


</body>
</html>