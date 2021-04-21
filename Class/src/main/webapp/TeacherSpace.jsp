<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page isELIgnored="false"%>


	<!DOCTYPE html>
	<html>
	<head>
	<link rel="stylesheet" type="text/css" href="./css/bootstrap.css">
	
	<meta charset="UTF-8">
	<title>Teacher Space</title>
	</head>
	<body>
	
	<div class="container-fluid bg-light">
		<div class="row bg-dark">
			<p>here you can execute many requests like showing classrom names </p>
		
		</div>
	
		<div class="row ">
				<div class="col-12">
					<div class="container">
							<form class="form-inline row" id="selection" action="DownloadForTeacher" method="post" > 
							<div class="row mt-3">
								<c:forEach items="${courseworks}" var="entry"><!--les classes-->
								  <div class="col-md-3">
									<div class="card shadow-sm">
									<h4>${entry.key.getName()}</h4>
									<div class="card-body">
									  <p class="card-text">
										<c:forEach items="${entry.value}" var="compterendu"> <!--les comptes rendu-->
										<input name="compterendu" type="checkbox" class="form-check-input" value="${compterendu.getId()},${entry.key.getId()}" id="${compterendu.getId()}">
										<label for="${compterendu.getId()}" class="form-check-label">${compterendu.getTitle()}</label>
										</p>
										</c:forEach>
									  <div class="d-flex justify-content-between align-items-center">
										<div class="btn-group">
										  <button type="button" class="btn btn-sm btn-outline-secondary" id="none">Uncheck all</button>
										  <button type="button" class="btn btn-sm btn-outline-success" id="all">Check all</button>
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
	<script src="./js/Tscript.js"></script>
	
	</body>
	</html>