package report.actions.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.util.AppUtil;

@WebServlet("/email")
public class EmailUpdatingServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	public EmailUpdatingServlet ()
	{
		super();
	}
	
	@Override
	protected void doGet (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
	{
		a_response.sendError(405, "Method Not Allowed");
	}
	 
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
		    	reportDir.renameTo(newReportDir);
		    }
		    else newReportDir.mkdir();
		}
		else
		{
			reportDir = new File (newReportPath);
			reportDir.mkdir();
		}
	}
}
