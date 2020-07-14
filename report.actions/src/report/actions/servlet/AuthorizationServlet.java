package report.actions.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.security.SecurityFilter;
import report.actions.util.AppUtil;
 
@WebServlet("/auth")
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
    	doForward(a_request, a_response, "/WEB-INF/views/authorizationView.jsp");
    }
 
    @Override
    protected void doPost (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException
    {
        String clientSendingToken = a_request.getParameter("token");
        String rightSendingToken = SecurityFilter.getToken(SecurityFilter.SENDING_TOKEN);
        
        if (!clientSendingToken.equals(rightSendingToken))
        {
        	String errorMessage = "Invalid token";
            a_request.setAttribute("errorMessage", errorMessage);
 
            doForward(a_request, a_response, "/WEB-INF/views/invalidTokenView.jsp");
            return;
        }
        
        AppUtil.storeToken(a_request.getSession(), clientSendingToken);
 
        int redirectId = -1;
        try 
        {
            redirectId = Integer.parseInt(a_request.getParameter("redirectId"));
        } 
        catch (Exception e) 
        {
        }
        String requestUri = AppUtil.getRedirectAfterLoginUrl(a_request.getSession(), redirectId);
        if (requestUri != null)  a_response.sendRedirect(requestUri);
        else a_response.sendRedirect(a_request.getContextPath() + "/main");
    }
    
    private void doForward (HttpServletRequest a_request, HttpServletResponse a_response, String a_jspPath) throws ServletException, IOException
    {
    	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(a_jspPath);
        dispatcher.forward(a_request, a_response);
    }
}