package report.server.main;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import report.server.security.config.ConfigurationFile;
import report.server.security.config.Configuration;

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
	public static void main(String[] args) throws IOException 
	{
		new Main().start();
	}
	
	private void start () throws IOException
	{
		Configuration conf =  new ConfigurationFile().getConfiguration();
		
		InetSocketAddress address = new InetSocketAddress(conf.getHostName(), conf.getPort());
		Server server = new Server(address);
		
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setBaseResource(Resource.newClassPathResource("webapp", true, true));
		webAppContext.setContextPath("/");
		
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
