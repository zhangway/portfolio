<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>


<script>
 function addRow()
 {
   var ptable = document.getElementById('operation');
   var lastElement = ptable.rows.length;
   var index = lastElement;
   var row = ptable.insertRow(index);


   var cellLeft = row.insertCell(0);
   var input = document.createElement('input');
   input.type = 'text';
   input.name = 'codeRef';
   input.id= 'codeRef';
   cellLeft.appendChild(input);

   var cellText = row.insertCell(1);
   var element = document.createElement('input');
   element.type = 'text';
   element.name = 'param';
   element.id = 'param';
   
   cellText.appendChild(element);
   document.getElementById("psize").value=index;
   }
 </script>
</head>
<body>
	<h1>Register an analytical service</h1>
	
	<form action="registerForm.htm">
		Name:        <input type="text" name="name"> <br>
		Description: <input type="text" name="description"><br> 
		
		<table id="operation" border="1">
			<caption>Operations</caption>
			<tr>
				<th>codeRef</th>
				<th>parameters</th>
				<th></th>
			</tr>

			<tr>
				<td><input type="text" name="codeRef" /></td>
				<td><input type="text" name="param" /></td>
				<td><input type="button" value="AddOperation" onclick="addRow();" /></td>
			</tr>
			
		</table>
		<input type="submit" value="Submit">
</form>


</body>
</html>