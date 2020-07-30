package jetty.reports.actions.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jetty.reports.actions.security.SecurityFilter;
import jetty.reports.actions.security.TokenStorage;
 
/**
 * Сервлет для обработки запросов авторизации.
 */
public class AuthorizationServlet extends HttpServlet 
{
    private static final long serialVersionUID = 1L;
    
    public AuthorizationServlet () 
    {
        super();
    }
 
    @Override
    protected void doGet (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
    {
    	doForward(a_request, a_response, "/WEB-INF/views/authorizationView.html");
    }
 
    @Override
    protected void doPost (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException
    {
        String clientSendingToken = a_request.getParameter("token");
        String rightSendingToken = SecurityFilter.getToken(SecurityFilter.DOWNLOADING_TOKEN);
        
        if (!clientSendingToken.equals(rightSendingToken))
        {
            doForward(a_request, a_response, "/WEB-INF/views/invalidTokenView.html");
            return;
        }
        
        TokenStorage.INSTANCE.storeToken(a_request.getSession(), clientSendingToken);
 
        String redirect = (String) a_request.getSession().getAttribute("redirect");
        String contextPath = ((HttpServletRequest)a_request).getContextPath();
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