package report.server.security.config;

/**
 * Конфигурация запуска сервера.
 */
public class Configuration
{
	protected String m_uploadingToken;
	protected String m_downloadingToken;
	protected int m_port;
	protected String m_hostName;
	
	/**
	 * @return токен для загрузки файла на сервер
	 */
	public String getUploadingToken ()
	{
		return m_uploadingToken;
	}
	
	/**
	 * @return токен для просмотра списка файлов
	 * на сервере и их скачивания
	 */
	public String getDownloadingToken ()
	{
		return m_downloadingToken;
	}
	
	public int getPort ()
	{
		return m_port;
	}
	
	public String getHostName ()
	{
		return m_hostName;
	}
}
