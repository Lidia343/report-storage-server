package report.actions.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.security.TokenStorage;
import report.actions.security.SecurityFilter;
 
/**
 * Сервлет для обработки запросов авторизации.
 */
@WebServlet("/auth")
public class AuthorizationServlet extends HttpServlet 
{
    private static final long serialVersionUID = 1L;
    
    public AuthorizationServlet () 
    {
        super();
    }
 
    /**
     * Перенаправляет на страницу авторизации.
     */
    @Override
    protected void doGet (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
    {
    	doForward(a_request, a_response, "/WEB-INF/views/authorizationView.jsp");
    }
 
    /**
     * Сравнивает токен, введённый пользователем, с установленным
     * токеном. Если они не совпадают, перенаправляет на страницу
     * авторизации с сообщением об ошибке. Если совпадают, то
     * сохраняет введённый токен в текущей сессии.
     */
    @Override
    protected void doPost (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException
    {
        String clientSendingToken = a_request.getParameter("token");
        String rightSendingToken = SecurityFilter.getToken(SecurityFilter.DOWNLOADING_TOKEN);
        
        if (!clientSendingToken.equals(rightSendingToken))
        {
        	String errorMessage = "Invalid token";
            a_request.setAttribute("errorMessage", errorMessage);
 
            doForward(a_request, a_response, "/WEB-INF/views/invalidTokenView.jsp");
            return;
        }
        
        TokenStorage.INSTANCE.storeToken(a_request.getSession(), clientSendingToken);
 
        String redirect = a_request.getParameter("redirect");
        String contextPath = a_request.getContextPath();
        if (redirect != null)  a_response.sendRedirect(contextPath + "/" + redirect);
        else a_response.sendRedirect(contextPath + "/main");
    }
    
    /**
     * Перенаправляет клиента на страницу, путь к которой равен a_jspPath.
     * @param a_request
     * 		  Запрос
     * @param a_response
     * 		  Ответ
     * @param a_jspPath
     * 		  Путь к jsp-странице
     * @throws ServletException
     * @throws IOException
     */
    private void doForward (HttpServletRequest a_request, HttpServletResponse a_response, String a_jspPath) throws ServletException, IOException
    {
    	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(a_jspPath);
        dispatcher.forward(a_request, a_response);
    }
}