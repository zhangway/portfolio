<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>  

<html>
<head>  
<script type="text/javascript" src="jquery-1.2.6.min.js"></script>  
<title>Import A Capability</title>  
</head> 
<body>

	<form:form method="post" enctype="multipart/form-data"  
   modelAttribute="uploadedFile" action="capUploaded.htm">  
   <table>  
    <tr>  
     <td>Import your capability file: </td>  
     <td><input type="file" name="file" />  
     </td>  
     <td style="color: red; font-style: italic;"><form:errors  
       path="file" />  
     </td>  
    </tr>  
    <tr>  
     <td> </td>  
     <td><input type="submit" value="Execute" />  
     </td>  
     <td> </td>  
    </tr>  
   </table> 
    
  </form:form>  
	
  
</body>
</html>