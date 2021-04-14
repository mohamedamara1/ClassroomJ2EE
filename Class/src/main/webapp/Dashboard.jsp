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

<form id="selection" action="DownloadFileServlet" method="post">

<input type="checkbox" id="all"> all
<table>
  <c:forEach items="${courses}" var="course">
    <tr>
     <td><input name="matieres" type="checkbox" value="${course.getId()}" > </td>
     <td>${course.getName()}</td>
    </tr>
  </c:forEach>
</table>
    <input type="button" value="Download" id="submit"/>
    
</form>
<script src="js/script.js"></script>
</body>
</html>