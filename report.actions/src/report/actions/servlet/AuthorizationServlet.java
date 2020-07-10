package report.actions.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.user.UserAccount;
import report.actions.util.AppUtil;
 
@WebServlet("/login")
public class LoginServlet extends HttpServlet 
{
    private static final long serialVersionUID = 1L;
 
    public LoginServlet() 
    {
        super();
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
        dispatcher.forward(request, response);
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
 
        UserAccount acc = new UserAccount(username, password);
        
        String name = "name";
        String pass = "123";
        if (!username.equals(name) && !password.equals(pass))
        {
        	String errorMessage = "Invalid userName or Password";
        	 
            request.setAttribute("errorMessage", errorMessage);
 
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
            return;
        }
        /*if (acc == null) 
        {
            String errorMessage = "Invalid userName or Password";
 
            request.setAttribute("errorMessage", errorMessage);
 
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
            return;
        }*/
 
        AppUtil.storeLoginedUser(request.getSession(), acc);
 
        int redirectId = -1;
        try 
        {
            redirectId = Integer.parseInt(request.getParameter("redirectId"));
        } 
        catch (Exception e) 
        {
        }
        String requestUri = AppUtil.getRedirectAfterLoginUrl(request.getSession(), redirectId);
        if (requestUri != null)  response.sendRedirect(requestUri);
        else response.sendRedirect(request.getContextPath() + "/mainView");
    }
}