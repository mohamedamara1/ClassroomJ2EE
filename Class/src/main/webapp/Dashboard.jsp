<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page isELIgnored="false"%>


<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="./css/bootstrap.css">

<meta charset="UTF-8">
<title>Dashboard</title>
</head>
<body>

<div class="container-fluid bg-light">
	<div class="row bg-dark">
		<p>here you can execute many requests like showing classrom names </p>
	
	</div>

	<div class="row ">
			<div class="offset-2 col-8">
				<div class=container bg-success">
						<form class="form-inline row" id="selection" action="DownloadFileServlet" method="post" onsubmit = "return myValidation();" >
						
						<div class="offset-4 col-4 ">
							<input  type="checkbox" id="all"> 
						  	<label for="all" class="form-check-label">Select all</label>

						</div>
						<div class="row mt-3">
							  <c:forEach items="${courses}" var="course">
							  <div class="col-md-6">
						  
						  		<input name="matieres" type="checkbox" class="form-check-input" value="${course.getId()}" id="${course.getId()}">
						  		<label for="${course.getId()}" class="form-check-label">${course.getName()}</label>
						  		
							  </div>
	
							  </c:forEach>					
						</div>
						
						<div class="row mt-3">
							<input class="offset-4 col-md-4 btn btn-sm btn-outline-success" type="submit" value="Download" id="submit">
							
						</div>

						    
						</form>
				
				</div>

			</div>
	</div>

</div>
<script src="./js/script.js"></script>

</body>
</html>