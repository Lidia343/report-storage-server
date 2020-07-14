package report.actions.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.util.AppUtil;

@WebServlet("/file/upload")
public class FileUploaderServlet extends HttpServlet 
{
	   private static final long serialVersionUID = 1L;
	   
	   public FileUploaderServlet () 
	   {
	      super();
	   }
	 
	   @Override
	   protected void doGet (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
	   {
		   String fileName = a_request.getParameter("file");
		   String archivePath = AppUtil.getReportArchivePath();
		   File archiveDir = new File(archivePath);
		   
		   boolean contain = false;
		   if (fileName != null)
		   {
			   for (File f : archiveDir.listFiles())
			   {
				   if (fileName.equals(f.getName())) 
				   {
					   contain = true;
					   break;
				   }
			   }
		   }
		   else
		   {
			   a_response.sendError(400, "Bad request");
			   return;
		   }
		   
		   if (!contain)
		   {
			   a_response.sendError(410, "Gone");
			   return;
		   }
		   
	       try (ServletOutputStream out = a_response.getOutputStream(); 
	    		InputStream in = new FileInputStream(archivePath + File.separator + fileName))
	       {
		       byte[] buffer = new byte[1024*64];
		       int length;
		       while ((length = in.read(buffer)) > 0)
		       {
		    	   out.write(buffer, 0, length);
		       }
		       a_response.setContentType("application/zip");
		       a_response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		       out.flush();
	       }
	   }
	 
	   @Override
	   protected void doPost (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
	   {
		   a_response.sendError(405, "Method Not Allowed");
	   }
}
