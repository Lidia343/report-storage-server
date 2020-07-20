package report.actions.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.util.AppUtil;
 
@WebServlet("/file/send")
public class FileSendingServlet extends HttpServlet 
{
   private static final long serialVersionUID = 1L;
   private String m_archivePath = "";
 
   public FileSendingServlet () throws IOException 
   {
      super();
      m_archivePath = AppUtil.getReportArchivePath();
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
	   
	   String email = AppUtil.getStringFromInputStream(in);
	   
	   String archivePath = m_archivePath + File.separator + email;
	   new File(archivePath).mkdir();
	   
	   String fileName = AppUtil.getStringFromInputStream(in);
	   
	   fileName = "report (" + AppUtil.getCurrentDateAndTime() + ").zip";
	  
	   File archive = new File (archivePath + File.separator + fileName);
	   archive.createNewFile();
	   
	   try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(archive)))
	   {
		   byte[] buffer = new byte[1024*64];
		   int length;
		   while ((length = in.read(buffer)) > 0)
		   {
			   out.write(buffer, 0, length);
		   }
	   }
   }
}
