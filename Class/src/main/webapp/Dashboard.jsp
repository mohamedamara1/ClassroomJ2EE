<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page isELIgnored="false"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard</title>
</head>
<body>
here you can execute many requests like showing classrom names <br>
<input type="checkbox" id="all"> all
<table>
  <c:forEach items="${courses}" var="course">
    <tr>
     <td><input type="checkbox" value="${course.getId()}" class ="classes"/> </td>
     <td>${course.getName()}</td>
    </tr>
  </c:forEach>
</table>

<form action="DownloadFileServlet" method="post">
    Enter Name: <input type="text" name="name" size="20">
    <input type="submit" value="Submit" />
</form>
<script src="js/script.js"></script>
</body>
</html>