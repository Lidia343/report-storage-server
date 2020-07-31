package report.server.main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import report.server.security.Configuration;

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
	
	private void createConfig () throws IOException
	{
		//Проверка файла в домашней директории (возможно, потом)
		//Пустые токены
		File configFile = new File("config.txt");
		if (configFile.exists()) return;
		configFile.createNewFile();
		
		try (OutputStream out = new BufferedOutputStream (new FileOutputStream (configFile)))
		{
			Gson gson = (new GsonBuilder().create());
			Configuration conf = new Configuration();
			conf.setUploadingToken("");
			//conf.set
		}
		
		
	}
}
