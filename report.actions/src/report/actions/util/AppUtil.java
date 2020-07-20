package report.actions.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
 
import javax.servlet.http.HttpSession;
 
public class AppUtil 
{
    private static int REDIRECT_ID = 0;
    private static final Map<Integer, String> m_id_uri_map = new HashMap<Integer, String>();
    private static final Map<String, Integer> m_uri_id_map = new HashMap<String, Integer>();
 
    public static void storeToken (HttpSession a_session, String a_token)
    {
        a_session.setAttribute("token", a_token);
    }
 
    public static String getToken (HttpSession a_session) 
    {
        return (String)a_session.getAttribute("token");
    }
 
    public static int storeRedirectAfterLoginUrl (HttpSession a_session, String a_requestUri) 
    {
        Integer id = m_uri_id_map.get(a_requestUri);
        if (id == null) 
        {
            id = REDIRECT_ID++;
            m_uri_id_map.put(a_requestUri, id);
            m_id_uri_map.put(id, a_requestUri);
            return id;
        }
        return id;
    }
 
    public static String getRedirectAfterLoginUrl (HttpSession a_session, int a_redirectId) 
    {
        String url = m_id_uri_map.get(a_redirectId);
        if (url != null) return url;
        return null;
    }
    
    public static String getStringFromInputStream (InputStream a_in) throws IOException
    {
    	String stringLengthLine = AppUtil.getNextStringFromInputStream(a_in, 2);
	    int stringLength = Integer.parseInt(stringLengthLine);
	    
		return AppUtil.getNextStringFromInputStream(a_in, stringLength);
    }
    
    private static String getNextStringFromInputStream (InputStream a_in, int a_stringLength) throws IOException
    {
    	String result = "";
    	for (int j = 0; j < a_stringLength; j++)
		{
    		result += Character.toString((char)a_in.read());
		}
    	return result;
    }
    
    public static String getReportArchivePath ()
    {
    	String path = System.getProperty("java.io.tmpdir") + File.separator + "reports";
    	File archiveDir = new File(path);
    	archiveDir.mkdir();
    	return path;
    }
    
    public static String getSubstringToCharacter (String a_line, char a_c)
    {
    	int end = a_line.indexOf(a_c);
		if (end != -1) a_line = a_line.substring(0, end);
		
		return a_line;
    }
    
    public static String getCurrentDateAndTime ()
	{
		Calendar calendar = GregorianCalendar.getInstance();
		
		String day = addZeroToString((Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))));
		String month =  addZeroToString(Integer.toString(calendar.get(Calendar.MONTH) + 1));
		String year = Integer.toString(calendar.get(Calendar.YEAR));
		
		String hour =  addZeroToString(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));
		String min =  addZeroToString(Integer.toString(calendar.get(Calendar.MINUTE)));
		String sec =  addZeroToString(Integer.toString(calendar.get(Calendar.SECOND)));
		
		return day + "-" + month + "-" + year + " - " + hour + "-" + min + "-" + sec;
	}
    
    public static String addZeroToString (String a_string)
	{
		return a_string.length() == 1 ? "0" + a_string : a_string;
	}
}