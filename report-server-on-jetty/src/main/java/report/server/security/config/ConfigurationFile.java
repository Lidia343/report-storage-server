package report.server.security.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Класс для получения конфигурации запуска сервера
 * из файла конфигурации.
 */
public class ConfigurationFile
{
	public static final String DIR_PATH = System.getProperty("user.home") +
		      							  File.separator + "report-server" +
		      							  File.separator;

	private static final String m_fileName = "config.json";

	public static final String FILE_PATH = DIR_PATH + m_fileName;

	private final String m_defaultDirPath = "configuration" + File.separator;
	private final String m_defaultFilePath = m_defaultDirPath + m_fileName;
	
	private ConfigurationChecker fileChecker = new ConfigurationChecker();
	
	/**
	 * @return конфигурацию запуска сервера.
	 * Конфигурация считывается из файла
	 * "config.json", который находится в
	 * папке "report-server" в домашней
	 * директории пользователя
	 * @throws IOException
	 */
	public Configuration getConfiguration () throws IOException 
	{
		mkUserAndDefaultDirs();
		if (!new File(FILE_PATH).exists())
		{
			return createUserFileAndGetConfig();
		}
		try
		{
			Configuration conf = readUserFile();
			if (!fileChecker.checkConfiguration(conf))
			{
				return createUserFileAndGetConfig();
			}
			return conf;
		}
		catch (JsonSyntaxException e)
		{
			return createUserFileAndGetConfig();
		}
	}
	
	/**
	 * Создаёт в домашней директории пользователя и в папке
	 * приложения директорию ("report-server" и "configuration"
	 * соответственно) для хранения файла конфигурации.
	 */
	private void mkUserAndDefaultDirs ()
	{
		new File(DIR_PATH).mkdir();
		new File(m_defaultDirPath).mkdir();
	}
	
	/**
	 * Удаляет пользовательский файл конфигурации
	 * (если он существует), создаёт файл конфигурации
	 * по умолчанию (если он ещё не был создан) в папке
	 * "configuration" в директории приложения и копирует
	 * данный файл в папку "report-server" в домашней
	 * директории пользователя.
	 * @return конфигурацию запуска сервера
	 * @throws IOException
	 */
	private Configuration createUserFileAndGetConfig () throws IOException
	{
		File defaultFile = new File(m_defaultFilePath);
		if (!defaultFile.exists())
		{
			defaultFile = createDefaultFile();
		}
		
		File userFile = new File(FILE_PATH);
		if (userFile.exists()) userFile.delete();
		Files.copy(defaultFile.toPath(), userFile.toPath());
		
		return readUserFile();
	}
	
	/**
	 * Создаёт файл конфигурации по умолчанию
	 * в папке "configuration" в директории
	 * приложения.
	 * @return конфигурационный файл по умолчанию
	 * @throws IOException
	 */
	private File createDefaultFile () throws IOException
	{
		File defaultFile = new File(m_defaultFilePath);
		defaultFile.createNewFile();
		
		try (Writer writer = new FileWriter (defaultFile))
		{
			Gson gson = (new GsonBuilder().create());
			DefaultConfiguration conf = DefaultConfiguration.INSTANCE;
			writer.write(gson.toJson(conf));
		}
		
		return defaultFile;
	}
	
	/**
	 * Считывает конфигурацию запуска сервера из
	 * конфигурационного файла, находящегося в папке
	 * "report-server" в домашней директории пользователя.
	 * Возвращаемая конфигурация должна быть проверена
	 * c помощью класса ConfigurationChecker.
	 * @return конфигурацию запуска сервера
	 * @throws IOException
	 * @throws JsonSyntaxException
	 */
	private Configuration readUserFile () throws IOException, JsonSyntaxException
	{
		try (BufferedReader reader = new BufferedReader (new FileReader(FILE_PATH)))
		{
			Gson gson = new GsonBuilder().create();
			
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			
			while ((line = reader.readLine()) != null)
			{
				stringBuilder.append(line);
			}
			
			return gson.fromJson(stringBuilder.toString(), Configuration.class);
		}
	}
}
