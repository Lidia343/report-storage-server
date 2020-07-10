<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
   <head>
      	<meta charset="UTF-8">
      	<title>Просмотр доступного файла</title>
   </head>
   <body>
   		<jsp:include page="unauthorizationView.jsp"></jsp:include>
   
    	<div><h1 align = 'center'><em><small>Просмотр доступных для загрузки архивов, содержащих отчёты об ошибках в коде САПР и информацию о системе пользователя</small></em></h1></div><hr>
    	
    	 <form action="${pageContext.request.contextPath}/file/upload" method = "get"
	         enctype = "multipart/form-data">
	         <input type = "file" name = "file" size = "75"/>
	         <br/>
	         <input type = "submit" value = "Загрузить"/>
         </form>
   </body>
</html>