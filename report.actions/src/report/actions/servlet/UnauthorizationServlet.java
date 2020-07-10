package report.actions.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/unauth")
public class UnauthorizationServlet extends HttpServlet 
{
    private static final long serialVersionUID = 1L;
 
    public UnauthorizationServlet() 
    {
        super();
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/");
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        this.doGet(request, response);
    }
}