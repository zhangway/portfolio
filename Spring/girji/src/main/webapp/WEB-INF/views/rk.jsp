<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>RunKeeper</title>
</head>
<body>
<p>
	User's RunKeeper Data
</p>


<table border="1">
<tr>
	<th>Date</th>
	<th>Type</th>
	<th>Distance(km)</th>
	<th>Duration</th>	
	<th>Calories</th>
</tr>
<c:forEach items="${feedItemsView}" var="FeedItems">
    <tr>
        <td>${FeedItems.startTime}</td>
        <td>${FeedItems.type}</td>
        <td>${FeedItems.totalDistance}</td>
        <td>${FeedItems.duration}</td>
        <td>${FeedItems.totalCalories}</td>
    </tr>
</c:forEach>
</table>

	<p>
	<a href="<c:url value="/welcome.htm"/>">Home</a>
	</p>

</body>
</html>
