package report.server.security.config;

/**
 * Конфигурация запуска сервера по умолчанию.
 * Токены данной конфигурации не могут быть
 * использованы сервером (пользователь должен
 * сам вводить их в файл).
 */
public class DefaultConfiguration extends Configuration
{
	public static final DefaultConfiguration INSTANCE = new DefaultConfiguration();
	
	public static final String UPLOADING_TOKEN = "";
	public static final String DOWNLOADING_TOKEN = "";
	public static final int PORT = 8080;
	public static final String HOST_NAME = "localhost";
	
	private DefaultConfiguration ()
	{
		m_uploadingToken = UPLOADING_TOKEN;
		m_downloadingToken = DOWNLOADING_TOKEN;
		m_port = PORT;
		m_hostName = HOST_NAME;
	}
}
