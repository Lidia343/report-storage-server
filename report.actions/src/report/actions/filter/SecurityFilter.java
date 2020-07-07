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
                String requestUri = request.getRequestURI();
                int redirectId = AppUtil.storeRedirectAfterLoginUrl(request.getSession(), requestUri);
                response.sendRedirect(wrapRequest.getContextPath() + "/login?redirectId=" + redirectId);
                return;
            } 
        }
 
        chain.doFilter(wrapRequest, response);
    }
 
    @Override
    public void init(FilterConfig fConfig) throws ServletException 
    {
    	
    }
}