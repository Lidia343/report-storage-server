<%@ page language='java' contentType='text/html; charset=UTF-8'
    pageEncoding='UTF-8'%>
<!DOCTYPE html>
<html>
   <head>
      <meta charset='UTF-8'>
      <title>Вход</title>
   </head>
   <body>

      <p style="color: red;">${errorString}</p>
 
      <form method='POST' action='${pageContext.request.contextPath}/auth'>
         <input type='hidden' name='redirect' value='${param.redirect}' />     
         <font size = 4> Токен: <input type='password' name='token' /></font><br>
		 <input type="submit" value= 'Войти' />
      </form>
      
   </body>
</html>