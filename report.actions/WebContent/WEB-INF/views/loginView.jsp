<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
   <head>
      <meta charset="UTF-8">
      <title>Вход</title>
   </head>
   <body>

      <p style="color: red;">${errorString}</p>
 
      <form method="POST" action="${pageContext.request.contextPath}/login">
         <input type="hidden" name="redirectId" value="${param.redirectId}" />     
         <font size = 4> Логин: <input type="text" name="username" value= "${user.username}" /></font><br>
		 <font size = 4> Пароль: <input type="password" name="password" value= "${user.password}" /></font><br>
		 <input type="submit" value= "Войти" />
      </form>
      
   </body>
</html>