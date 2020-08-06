package report.server.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import report.server.security.config.ConfigurationFile;
 
/**
 * Фильтр, который проверяет каждый запрос, который должен быть
 * обработан сервлетом.
 */
public class SecurityFilter implements Filter 
{	
    @Override
    public void destroy () 
    {
    }
 
    /**
     * Если клиент пытается получить доступ к несуществующей странице,
     * метод отправляет сообщение об ошибке 404.
     * Если клиент неавторизован, в случае попытки загрузки файла на 
     * сервер метод отправляет ошибку 401, а в случае попытки просмотреть 
     * файлы для скачивания перенаправляет на страницу авторизации.
     */
    @Override
    public void doFilter (ServletRequest a_request, ServletResponse a_response, FilterChain a_chain) throws IOException, ServletException 
    {
        HttpServletRequest request = (HttpServletRequest) a_request;
        HttpServletResponse response = (HttpServletResponse) a_response;
 
        String servletPath = request.getServletPath();
        if (!servletPath.equals("/") && 
        	!servletPath.equals("/auth") && 
        	!servletPath.equals("/unauth") && 
        	!servletPath.equals("/main") &&
        	!servletPath.equals("/file") &&
            !servletPath.equals("/file/upload") &&
            !servletPath.equals("/file/download") &&
            !servletPath.equals("/email"))
        {
        	response.sendError(404, "Not found");
			return;
        }
        
        if (servletPath.equals("/auth")) 
        {
            a_chain.doFilter(request, response);
            return;
        }
        
        if (servletPath.contains("/file") || servletPath.equals("/email"))
        {
        	HttpSession session = request.getSession();
        	String clientDownloadingToken = TokenStorage.INSTANCE.getToken(session);
        	if (clientDownloadingToken == null)
        	{
        		if (request.getMethod().equals("POST"))
        		{
        			String rightUploadingToken = new ConfigurationFile().getConfiguration().getUploadingToken();
        			if (!request.getHeader("Authorization").equals(rightUploadingToken))
        			{
        				   response.sendError(401, "Unauthorized");
        				   return;
        			}
        		}
        		if (request.getMethod().equals("GET") && !servletPath.equals("/email"))
	            {
        			session.setAttribute("redirect", servletPath.substring(1, servletPath.length()));
	                response.sendRedirect(request.getContextPath() + "/auth?redirect=" + 
	                                      servletPath.substring(1, servletPath.length()));
	                return;
	            } 
        	}
        }
        a_chain.doFilter(request, response);
    }
 
    @Override
    public void init (FilterConfig a_config) throws ServletException 
    {
    	
    }
}