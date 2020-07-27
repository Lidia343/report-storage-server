package report.actions.security;

import javax.servlet.http.HttpSession;

/**
 * Хранилище токена.
 */
public class TokenStorage 
{
	public static final TokenStorage INSTANCE = new TokenStorage();
	
	private TokenStorage ()
	{
		
	}
    
    /**
     * Устанавливает токен в текущую сессию.
     * @param a_session
     * 		  Сессия
     * @param a_token
     * 		  Токен для установки
     */
    public void storeToken (HttpSession a_session, String a_token)
    {
        a_session.setAttribute("token", a_token);
    }
 
    /**
     * @param a_session
     * 		  Сессия
     * @return токен из текущей сессии
     */
    public String getToken (HttpSession a_session) 
    {
        return (String)a_session.getAttribute("token");
    }
}
