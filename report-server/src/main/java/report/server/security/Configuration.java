package report.server.security;

public class Configuration 
{
	private String m_uploadingToken;
	private String m_downloadingToken;
	private String m_port;
	private String m_hostName;
	
	public void setDownloadingToken (String a_downLoadingToken)
	{
		m_downloadingToken = a_downLoadingToken;
	}
	
	public String getDownloadingToken ()
	{
		return m_uploadingToken;
	}
	
	public void setUploadingToken (String a_uploadingToken)
	{
		m_uploadingToken = a_uploadingToken;
	}
	
	public String getUploadingToken ()
	{
		return m_downloadingToken;
	}
	
	public void setPort (String a_port)
	{
		m_port = a_port;
	}
	
	public String getPort ()
	{
		return m_port;
	}
	
	public void setHostName (String a_hostName)
	{
		m_hostName = a_hostName;
	}
	
	public String getHostName ()
	{
		return m_hostName;
	}
}
