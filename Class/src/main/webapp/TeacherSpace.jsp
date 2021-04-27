<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page isELIgnored="false"%>


<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="./css/bootstrap.css">
<link ref="stylesheet" type="text/css" href="./css/teacher.css">
<meta charset="UTF-8">
<title>Teacher Space</title>
</head>
<body>

<div class="container-fluid bg-light h-100">

	<div class="row justify-content-center h-100 ">
			<div class="col-12 h-100">
				<div class="container h-100">
						<form class="form-inline row h-100" id="selection" action="DownloadForTeacher" method="post" onsubmit = "return myValidation();"> 
						<div class="row h-100 mt-3">
							<c:forEach items="${courseworks}" var="entry"><!--les classes-->
							  <div class="col-md-3">
								<div class="card shadow-sm">
								<h4>${entry.key.getName()}</h4>
								
								<div class="card-body">
								  <p class="card-text">
									<c:forEach items="${entry.value}" var="compterendu"> <!--les comptes rendu-->
									<input name="compterendu" type="checkbox" class="form-check-input ${entry.key.getId()}" value="${compterendu.getId()},${entry.key.getId()}" id="${compterendu.getId()}">
									<label for="${compterendu.getId()}" class="form-check-label">${compterendu.getTitle()}</label>
									</p>
									</c:forEach>
								  <div class="d-flex justify-content-between align-items-center">
									<div class="btn-group">
									  <button name="${entry.key.getId()}" type="button" class="btn btn-sm btn-outline-secondary" name="uncheckall">Uncheck all</button>
									 <button name="${entry.key.getId()}" type="button" class="btn btn-sm btn-outline-success" name="checkall">Check all</button> 
									</div>
									</div>
								  </div>
								</div>
							  </div>
	
							  </c:forEach>					
						</div>
						
						<div class="row mt-3">
							<input class="offset-4 col-md-4 btn-outline-success" type="submit" value="Download" id="submit">

							
						</div>							
						</form>
				
				</div>

			</div>
	</div>

</div>
<script src="./js/tscript.js"></script>

</body>
</html>