package report.actions.security;

import javax.servlet.http.HttpSession;

public class TokenStorage 
{
	public static final TokenStorage INSTANCE = new TokenStorage();
	
	private TokenStorage ()
	{
		
	}
    
    public void storeToken (HttpSession a_session, String a_token)
    {
        a_session.setAttribute("token", a_token);
    }
 
    public String getToken (HttpSession a_session) 
    {
        return (String)a_session.getAttribute("token");
    }
}
