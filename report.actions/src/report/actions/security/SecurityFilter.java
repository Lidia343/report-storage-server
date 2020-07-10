package report.actions.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.util.AppUtil;
 
@WebFilter("/*")
public class SecurityFilter implements Filter 
{
	public static final String RECEIVING_TOKEN_FILE_NAME = "reportReceivingToken.txt";
	public static final String SENDING_TOKEN_FILE_NAME = "reportSendingToken.txt";
	
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
 
        String sendingToken = AppUtil.getToken(request.getSession());
 
        if (servletPath.equals("/auth")) 
        {
            chain.doFilter(request, response);
            return;
        }
        
        HttpServletRequest wrapRequest = request;
        if (request.getServletPath().contains("/file"))
        {
        	if (sendingToken == null)
        	{
        		if (request.getMethod().equals("GET"))
	            {
	                String requestUri = request.getRequestURI();
	                int redirectId = AppUtil.storeRedirectAfterLoginUrl(request.getSession(), requestUri);
	                response.sendRedirect(wrapRequest.getContextPath() + "/auth?redirectId=" + redirectId);
	                return;
	            } 
        		if (request.getMethod().equals("POST"))
        		{
        			if (!request.getHeader("Authorization").equals(getToken(RECEIVING_TOKEN_FILE_NAME)))
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
    
    public String getToken (String a_tokenFileName) throws IOException
    {
    	try (InputStream in = getClass().getClassLoader().getResourceAsStream(a_tokenFileName);
   		     BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
   		{
   			String token = reader.readLine();
   			if (token.endsWith("\r\n")) 
   				token = token.substring(0, token.length());
   			return token;
   		}
    }
}