package report.actions.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
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
	   
	   public FileUploaderServlet() 
	   {
	      super();
	   }
	 
	   @Override
	   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	   {
		   String fileName = request.getParameter("file");
		   if (fileName == null || fileName.equals(""))
		   {
			   RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/existingFileView.jsp");
		       dispatcher.forward(request, response);
		       return;
		   }
		   
	       try (ServletOutputStream out = response.getOutputStream(); 
	    		InputStream in = new FileInputStream(AppUtil.getReportArchivePath() + "\\" + fileName))
	       {
		       byte[] buffer = new byte[1024*64];
		       int length;
		       while ((length = in.read(buffer)) > 0)
		       {
		    	   out.write(buffer, 0, length);
		       }
		       response.setContentType("application/zip");
		       response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		       out.flush();
	       }
	   }
	 
	   @Override
	   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	   {
		   
	   }
}
