package jetty.reports.actions.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/**
 * Сервлет для перенаправления клиента на главную страницу
 * /WEB-INF/views/mainView.html.
 */
public class MainServlet extends HttpServlet 
{
    private static final long serialVersionUID = 1L;
 
    public MainServlet() 
    {
        super();
    }
 
    @Override
    protected void doGet (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
    {
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/mainView.html");
        dispatcher.forward(a_request, a_response);
    }
 
    @Override
    protected void doPost (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
    {
        doGet(a_request, a_response);
    }
}