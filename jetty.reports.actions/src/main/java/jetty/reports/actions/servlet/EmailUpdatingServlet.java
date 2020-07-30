package jetty.reports.actions.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jetty.reports.actions.util.AppUtil;

/**
 * Сервлет для обработки запросов загрузки старого и нового
 * значений адреса электронной почты пользователя на сервер.
 */
public class EmailUpdatingServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	public EmailUpdatingServlet ()
	{
		super();
	}
	 
	/**
	 * Если папка, соответствующая старому значению email, существует,
	 * она переименовывается в новое значение email. Если не существует,
	 * создаётся папка с именем, равным новому значению email.
	 */
	@Override
	protected void doPost (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException, NumberFormatException 
	{
		InputStream in = a_request.getInputStream();
		    
		String oldEmail = AppUtil.getStringFromInputStream(in);
		String newEmail = AppUtil.getStringFromInputStream(in);
		
		if (!AppUtil.checkEmail(oldEmail) || !AppUtil.checkEmail(newEmail))
		 {
			   a_response.sendError(400, "Bad request");
			   return;
		 }
		
		String reportPathPart = AppUtil.getReportArchivePath() + File.separator;
		String newReportPath = reportPathPart + newEmail;
		File reportDir;
		if (!oldEmail.equals(""))
		{
		    reportDir = new File (reportPathPart + oldEmail);
		    File newReportDir = new File(newReportPath);
		    if (reportDir.exists())
		    {
		    	reportDir.renameTo(newReportDir); //Переименование папки
		    }
		    /*Создание новой папки в случае, когда папка, соответствующая
		      старому адресу электронной почты, не была найдена:*/
		    else newReportDir.mkdir();
		}
		else
		{
			/*Создание новой папки в случае, когда старый адрес электронной
			  почты равен пустой строке:*/
			reportDir = new File (newReportPath);
			reportDir.mkdir();
		}
	}
}
