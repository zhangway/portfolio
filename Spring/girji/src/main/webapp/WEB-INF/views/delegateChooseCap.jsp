<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>  

<html>
<head>  
<script type="text/javascript" src="jquery-1.2.6.min.js"></script>  
<title>Capability Delegation</title>  
</head> 
<body>

	<h1>Delegate a Cap</h1>
	<form:form method="post" enctype="multipart/form-data"
		modelAttribute="uploadedFile" action="delegatecapUploaded.htm">
		<table>
			<tr>
				<td>Select a Capability file:</td>
				<td><input type="file" name="file" /></td>
				<td style="color: red; font-style: italic;"><form:errors
						path="file" /></td>
			</tr>
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
				<td>codeRef: param</td>
				<td><input type="text" name="codeRef" /></td>
				<td><input type="text" name="param1" /></td>
				<td><input type="text" name="param2" /></td>
			</tr>
			<tr>
				<td>AccessPeriod</td>
				<td><input type="text" name="accessPeriod" /></td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Submit" /></td>
				<td></td>
			</tr>
		</table>


	</form:form>

	
  
</body>
</html>