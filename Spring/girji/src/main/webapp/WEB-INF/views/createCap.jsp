<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
<script type="text/javascript" src="jquery-1.2.6.min.js"></script>
<title>Create A Capability</title>
</head>
<body>
	<h1>${message}create a cap</h1>
	<form:form method="post" enctype="multipart/form-data"
		modelAttribute="uploadedFile" action="fileUpload.htm">
		<table>
			<tr>
				<td>Upload R Source File:</td>
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
				<td>AccessPeriod</td>
				<td><input type="text" name="accessPeriod" /></td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Upload" /></td>
				<td></td>
			</tr>
		</table>


	</form:form>


</body>
</html>