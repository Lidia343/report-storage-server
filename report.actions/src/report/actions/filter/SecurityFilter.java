package report.actions.filter;

import java.io.IOException;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.user.UserAccount;
import report.actions.util.AppUtil;
 
@WebFilter("/*")
public class SecurityFilter implements Filter 
{
	private final String m_token = "4r3rSw4654wyb3aEg4Fqq6454qwEbh6q346qGm8emxgok9E8543e";
	
    @Override
    public void destroy() 
    {
    	
    }
 
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException 
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
 
        String servletPath = request.getServletPath();
 
        UserAccount loginedUser = AppUtil.getLoginedUser(request.getSession());
 
        if (servletPath.equals("/login")) 
        {
            chain.doFilter(request, response);
            return;
        }
        
        HttpServletRequest wrapRequest = request;
        if (request.getServletPath().contains("/file"))
        {
        	if (loginedUser == null)
        	{
        		if (request.getMethod().equals("GET"))
	            {
	                String requestUri = request.getRequestURI();
	                int redirectId = AppUtil.storeRedirectAfterLoginUrl(request.getSession(), requestUri);
	                response.sendRedirect(wrapRequest.getContextPath() + "/login?redirectId=" + redirectId);
	                return;
	            } 
        		if (request.getMethod().equals("POST"))
        		{
        			if (!request.getHeader("Authorization").equals(m_token))
        			{
        				   response.sendError(401, "Unauthorized");
        				   return;
        			}
        		}
        	}
        }
 
        chain.doFilter(wrapRequest, response);
    }
 
    @Override
    public void init(FilterConfig fConfig) throws ServletException 
    {
    	
    }
}