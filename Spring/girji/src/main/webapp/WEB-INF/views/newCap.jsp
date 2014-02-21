<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>

<title>New Capability</title>
</head>
<body>
	<h1>${message} New Capability</h1>
	<form:form method="post" enctype="multipart/form-data" action="capNew.htm">
		<table>
			
			<tr>
				<td>Name</td>
				<td><input type="text" name="name" /></td>
				<td></td>
			</tr>
			<tr>
				<td>Description</td>
				<td><input type="text" name="description" /></td>
				<td></td>
			</tr>
			<tr>
				<td>Code Ref</td>
				<td><input type="text" name="codeRef" /></td>
				<td></td>
			</tr>
			<tr>
				<td>AccessPeriod</td>
				<td><input type="text" name="accessPeriod" /></td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Create" /></td>
				<td></td>
			</tr>
		</table>


	</form:form>


</body>
</html>