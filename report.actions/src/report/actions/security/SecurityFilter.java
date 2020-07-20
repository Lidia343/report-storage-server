package report.actions.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
	public static final int RECEIVING_TOKEN = 0;
	public static final int SENDING_TOKEN = 1;
	
	private static final String m_configFilePathPart = "report-storage-server" + File.separator + "config.txt";
	
    @Override
    public void destroy () 
    {
    }
 
    @Override
    public void doFilter (ServletRequest a_request, ServletResponse a_response, FilterChain a_chain) throws IOException, ServletException 
    {
        HttpServletRequest request = (HttpServletRequest) a_request;
        HttpServletResponse response = (HttpServletResponse) a_response;
 
        String servletPath = request.getServletPath();
        if (!servletPath.equals("/auth") && 
        	!servletPath.equals("/unauth") && 
        	!servletPath.equals("/main") && 
        	!servletPath.equals("/file") &&
            !servletPath.equals("/file/upload") &&
            !servletPath.equals("/file/send") &&
            !servletPath.equals("/email") &&
            !servletPath.equals("/"))
        {
        	response.sendError(404, "Not found");
			return;
        }
        
        if (servletPath.equals("/auth")) 
        {
            a_chain.doFilter(request, response);
            return;
        }
        
        HttpServletRequest wrapRequest = request;
        servletPath = request.getServletPath();
        if (servletPath.contains("/file") || servletPath.contains("/email"))
        {
        	String sendingToken = AppUtil.getToken(request.getSession());
        	if (sendingToken == null)
        	{
        		if (request.getMethod().equals("POST"))
        		{
        			if (!request.getHeader("Authorization").equals(getToken(RECEIVING_TOKEN)))
        			{
        				   response.sendError(401, "Unauthorized");
        				   return;
        			}
        		}
        		if (request.getMethod().equals("GET") && !servletPath.contains("/email"))
	            {
	                String requestUri = request.getRequestURI();
	                int redirectId = AppUtil.storeRedirectAfterLoginUrl(request.getSession(), requestUri);
	                response.sendRedirect(wrapRequest.getContextPath() + "/auth?redirectId=" + redirectId);
	                return;
	            } 
        	}
        }
        a_chain.doFilter(wrapRequest, response);
    }
 
    @Override
    public void init (FilterConfig a_config) throws ServletException 
    {
    	
    }
    
    public static String getToken (int a_tokenType) throws IOException
    {
    	try (FileInputStream in = new FileInputStream (System.getProperty("user.home") + File.separator + m_configFilePathPart);
   		     BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
   		{
   			String token = reader.readLine();
   			if (a_tokenType == SENDING_TOKEN) token = reader.readLine();
   			
   			token = AppUtil.getSubstringToCharacter(token, '/');
   			token = AppUtil.getSubstringToCharacter(token, '\t');
   			token = AppUtil.getSubstringToCharacter(token, ' ');
   			token = AppUtil.getSubstringToCharacter(token, System.lineSeparator().charAt(0));
   			
   			return token;
   		}
    }
}