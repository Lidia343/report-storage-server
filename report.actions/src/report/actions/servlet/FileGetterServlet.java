package report.actions.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.util.AppUtil;
 
@WebServlet("/file")
public class FileGetterServlet extends HttpServlet 
{
   private static final long serialVersionUID = 1L;
   private final int m_lengthOfSizeLine = 2;
   private String m_archivePath = "";
 
   public FileGetterServlet () throws IOException 
   {
      super();
      m_archivePath = AppUtil.getReportArchivePath();
   }
   
   @Override
   protected void doGet (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
   {
	   File archiveDir = new File(m_archivePath);
	   File[] files = archiveDir.listFiles();
	   int fileNum = files.length;
	   
	   a_response.setContentType("text/html");  
	   a_response.setCharacterEncoding("UTF-8");
	   try(PrintWriter out = a_response.getWriter())
	   {
		   out.println("<html>");
		   if (fileNum != 0) 
		   {
			   out.println("\t<head><meta charset='UTF-8'><title>Выбор файлов</title></head>");
			       
			   out.println("\t<body>");
			   out.println("\t\t<jsp:include page='unauthorizationView.jsp'></jsp:include>");
			   out.println("");
			   out.println("\t\t<div><p>Выберите файл для загрузки:</p></div><hr>");
			   out.println("");
			   
			   for (File f : files)
			   {
				   out.println("\t\t<form action='" + a_request.getRequestURL() + "/upload" + "' method = 'get' enctype = 'multipart/form-data'>");
			       out.println("\t\t<input type = 'text' name = 'file' value = '" + f.getName() + "' readonly = 'readonly' size = '31'/>");
			       out.println("\t\t<input type = 'submit' value = 'Загрузить'>");
			       out.println("\t\t</form>");
			       out.println("");
			   }   
		   }
		   else
		   {
			   out.println("\t<head><meta charset='UTF-8'><title>Файлы не найдены</title></head>");
		       
			   out.println("\t<body>");
			   out.println("\t\t<jsp:include page='unauthorizationView.jsp'></jsp:include>");
			   out.println("");
			   out.println("\t\t<div><h1 align = 'center'><em><small>Доступные для загрузки архивы не найдены.</small></em></h1></div><hr>");
			   out.println("\t\t<div><h1 align = 'center'><em><small><a href = 'file'>Проверить наличие архива для загрузки</a></small></em></h1></div><hr>");
			   out.println("");
		   }
		   out.println("</body>");
		   out.println("</html>");
	   }
   }
 
   @Override
   protected void doPost (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException, NumberFormatException 
   {
	   InputStream in = a_request.getInputStream();
	   
	   String fileNameLengthLine = AppUtil.getNextStringFromInputStream(in, m_lengthOfSizeLine);
	   int fileNameLength = Integer.parseInt(fileNameLengthLine);
	   
	   String fileName = AppUtil.getNextStringFromInputStream(in, fileNameLength);
	  
	   File archive = new File (m_archivePath + "\\" + fileName);
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
