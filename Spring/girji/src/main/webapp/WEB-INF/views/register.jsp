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
   element.name = 'param1';
   element.id = 'param1';
   
   cellText.appendChild(element);
   
   var cellText2 = row.insertCell(2);
   var element2 = document.createElement('input');
   element2.type = 'text';
   element2.name = 'param2';
   element2.id = 'param2';
   
   cellText2.appendChild(element2);
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
				<th>parameter1</th>
				<th>parameter2</th>
				<th></th>
			</tr>

			<tr>
				<td><input type="text" name="codeRef" /></td>
				<td><input type="text" name="param1" /></td>
				<td><input type="text" name="param2" /></td>
				<td><input type="button" value="AddOperation" onclick="addRow();" /></td>
			</tr>
			
		</table>
		<input type="submit" value="Submit">
</form>


</body>
</html>