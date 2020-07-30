package jetty.reports.actions.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jetty.reports.actions.util.AppUtil;

/**
 * Сервлет для обработки запросов скачивания файла с сервера.
 */
public class FileDownloadingServlet extends HttpServlet 
{
	   private static final long serialVersionUID = 1L;
	   
	   public FileDownloadingServlet () 
	   {
	      super();
	   }
	   
       /**
	   * Передаёт клиенту выбранный им файл.
	   */
	   @Override
	   protected void doGet (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
	   {
		   String email = a_request.getParameter("email");
		   String fileName = a_request.getParameter("file");
		   
		   String archivePath = AppUtil.getReportArchivePath() + File.separator + email;
		   File archiveDir = new File(archivePath);
		   
		   boolean contain = false;
		   if (fileName != null)
		   {
			   //Поиск в директории archiveDir файла с именем fileName:
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
		   
		   if (!contain) //Если выбранный файл не был найден, отправляется ошибка 410:
		   {
			   a_response.sendError(410, "Gone");
			   return;
		   }
		   
		   //Отправка выбранного архива клиенту:
		   a_response.setContentType("application/zip");
	       a_response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		   
	       try (ServletOutputStream out = a_response.getOutputStream(); 
		    	InputStream in = new FileInputStream(archivePath + File.separator + fileName))
	       {
	    	   AppUtil.writeInputStreamToOutputStream(in, out, AppUtil.MAX_ARCHIVE_SIZE, false);
	       }
	   }
}
