package report.actions.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
 
import javax.servlet.http.HttpSession;

import report.actions.user.UserAccount;
 
public class AppUtil 
{
    private static int REDIRECT_ID = 0;
    private static final Map<Integer, String> id_uri_map = new HashMap<Integer, String>();
    private static final Map<String, Integer> uri_id_map = new HashMap<String, Integer>();
 
    public static void storeLoginedUser(HttpSession session, UserAccount loginedUser)
    {
        session.setAttribute("loginedUser", loginedUser);
    }
 
    public static UserAccount getLoginedUser(HttpSession session) 
    {
        UserAccount loginedUser = (UserAccount) session.getAttribute("loginedUser");
        return loginedUser;
    }
 
    public static int storeRedirectAfterLoginUrl(HttpSession session, String requestUri) 
    {
        Integer id = uri_id_map.get(requestUri);
 
        if (id == null) 
        {
            id = REDIRECT_ID++;
            uri_id_map.put(requestUri, id);
            id_uri_map.put(id, requestUri);
            return id;
        }
        return id;
    }
 
    public static String getRedirectAfterLoginUrl(HttpSession session, int redirectId) 
    {
        String url = id_uri_map.get(redirectId);
        if (url != null) return url;
        return null;
    }
 
    public static String getNextStringFromInputStream (InputStream a_stream, int a_stringLength) throws IOException
    {
    	String result = "";
    	for (int j = 0; j < a_stringLength; j++)
		{
    		result += Character.toString((char)a_stream.read());
		}
    	return result;
    }
    
    public static String getReportArchivePath ()
    {
       String a_path = System.getProperty("user.home") + "\\reports";
  	   File archiveDir = new File(a_path);
  	   archiveDir.mkdir();
  	   return a_path += "\\";
    }
}