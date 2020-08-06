package report.server.security.config;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Класс для проверки конфигурации запуска сервера.
 */
public class ConfigurationChecker
{
	/**
	 * @param a_conf
	 * 		  Конфигурация запуска сервера
	 * @return true - если значения объекта конфигурации,
	 * его токенов и имени хоста не равны null, и если
	 * имя хоста - "localhost" либо IP-адрес. false - иначе
	 * @throws IOException в случае, если хотя бы один токен
	 * в файле конфигурации равен пустой строке
	 */
	public boolean checkConfiguration (Configuration a_conf) throws IOException
	{
		String uploadingToken;
		String downloadingToken;
		if (a_conf == null || (downloadingToken = 
			a_conf.getDownloadingToken()) == null ||
		    (uploadingToken = a_conf.getUploadingToken()) ==
		    null || a_conf.getHostName() == null)
		{
			return false;
		}
		if (uploadingToken.equals("") || downloadingToken.equals(""))
		{
			throw new IOException("Введите значения токенов в файл конфигурации.");
		}
		return isHostName(a_conf.getHostName());
	}

	/**
	 * Проверяет переданное значение a_name на соответствие
	 * шаблону IP-адреса.
	 * @param a_name
	 * 		  Строка для проверки
	 * @return если a_name является IP-адресом либо строкой
	 * "localhost" - true, иначе - false
	 */
	private boolean isHostName (String a_name)
	{
		if (a_name.equals(DefaultConfiguration.HOST_NAME))
		{
			return true;
		}
		String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(a_name).matches();
	}
}
