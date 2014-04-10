<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>



</head>
<body>
	<h1>Set constraint</h1>

	<form action="finish.htm">

		<input type="hidden" name="name" value="${reqView.name}" />
		<p> Name: ${reqView.name} </p>
		<p> Description: ${reqView.description} </p>
		
		<table id="operation" border="1">
			<caption>Operations</caption>
			<tr>

				<th>codeRef</th>
				<th>parameter</th>

			</tr>

			<c:forEach items="${reqView.operations}" var="o">
				<tr>

					<td>${o.codeRef}</td>
					<td>${o.param}</td>

				</tr>
			</c:forEach>



		</table>
		
			accessPeriod: <input type="text" name="accessPeriod">
		
		<P>
			allowDelegation: <input type="radio" name="allow" value="true">yes
			<input type="radio" name="allow" value="false">no
		</P>
		<input type="submit" value="Submit">
	</form>


</body>
</html>