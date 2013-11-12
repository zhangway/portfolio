<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
  <title>tonr</title>
</head>
<body>
<div id="container">

    <ul id="mainlinks">
      <li><a href="<c:url value="/index.jsp"/>" class="selected">home</a></li>
      <authz:authorize ifNotGranted="ROLE_USER">
        <li><a href="<c:url value="/login.jsp"/>">login</a></li>
      </authz:authorize>
      <li><a href="<c:url value="/connectrunkeeper"/>">Connect to RunKeeper</a></li>
    
    </ul>

  <div id="content">
    <h1>Welcome to Tonr.com!</h1>
    
  

    <p>Tonr.com has only two users: "marissa" and "sam".  The password for "marissa" is password is "wombat" and for "sam" is password is "kangaroo".</p>

    <authz:authorize ifNotGranted="ROLE_USER">
      <p><a href="<c:url value="login.jsp"/>">Login to Tonr</a></p>
    </authz:authorize>
    <authz:authorize ifAllGranted="ROLE_USER">
      <p><a href="<c:url value="/connectrunkeeper"/>">Connect to RunKeeper</a></p>
    </authz:authorize>

    
  </div>
</div>
</body>
</html>