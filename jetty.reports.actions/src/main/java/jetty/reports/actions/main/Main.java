package jetty.reports.actions.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Главный класс приложения, содержащий точку входа в программу.
 */
public class Main 
{
	/**
	 * Точка входа в программу.
	 * @param args
	 *        Аргументы программы
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception 
	{
		Server server = new Server(8080);
		
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setParentLoaderPriority(true);
		webAppContext.setContextPath("/reports.actions/");
		webAppContext.setExtractWAR(true);    
		webAppContext.setWar("src/main/webapp");
		server.setHandler(webAppContext);
		
		try
		{
			server.start();
			server.join();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
