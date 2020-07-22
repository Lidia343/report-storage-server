package report.actions.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.comp.FileListSorter;
import report.actions.util.AppUtil;
 
@WebServlet("/file")
public class FileViewServlet extends HttpServlet 
{
   private static final long serialVersionUID = 1L;
   private String m_archivePath = "";
 
   public FileViewServlet () throws IOException 
   {
      super();
      m_archivePath = AppUtil.getReportArchivePath();
   }
   
   @Override
   protected void doGet (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException 
   {
	   List<File> files = AppUtil.getAllArchives(m_archivePath);
	   
	   Collections.sort(files, new FileListSorter());
	   
	   int fileNum = files.size();
	   
	   a_response.setContentType("text/html");
	   a_response.setCharacterEncoding("UTF-8");
	   try (PrintWriter out = a_response.getWriter())
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
				   String email = f.getParentFile().getName();
				   String fileName = f.getName();
				   out.println("\t\t<form action='" + a_request.getRequestURL() + "/download" + "' method = 'get' enctype = 'multipart/form-data'>");
				   out.println("\t\t<input type = 'hidden' name = 'email' value = '" + email + "'/>");
				   out.println("\t\t<input type = 'hidden' name = 'file' value = '" + fileName + "'/>");
			       out.println("\t\t<input type = 'text' name = 'emailAndFile' value = '" + email + ": " + fileName + "' readonly = 'readonly' size = '60'/>");
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
}
