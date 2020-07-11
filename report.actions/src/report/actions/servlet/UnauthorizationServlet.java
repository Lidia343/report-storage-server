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
 
    public UnauthorizationServlet () 
    {
        super();
    }
 
    @Override
    protected void doGet (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
    {
        a_request.getSession().invalidate();
        a_response.sendRedirect(a_request.getContextPath() + "/auth");
    }
 
    @Override
    protected void doPost (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
    {
        this.doGet(a_request, a_response);
    }
}