package jetty.reports.actions.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jetty.reports.actions.comp.FileListSorter;
import jetty.reports.actions.util.AppUtil;
 
/**
 * Сервлет для обработки запросов загрузки файла
 * на сервер.
 */
public class FileUploadingServlet extends HttpServlet 
{
   private static final long serialVersionUID = 1L;
   
   private String m_archivePath = "";
 
  /**
   * Вызывает конструктор класса HttpServlet и
   * устанавливает путь к папке, в которой
   * находятся папки с архивами.
   */
   public FileUploadingServlet ()
   {
      super();
      m_archivePath = AppUtil.getReportArchivePath();
   }
   
   /**
    * Если если имя файла и email пользователя
    * корректны, а сам файл соответствует
    * расширению ".zip" и проходит проверку на
    * размер, метод загружает его на сервер.
 	*/
   @Override
   protected void doPost (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException, NumberFormatException 
   {
	   InputStream in = a_request.getInputStream();
	   
	   String email = AppUtil.getStringFromInputStream(in);
	   String archiveName = AppUtil.getStringFromInputStream(in);
	   
	   //Проверка полученных имени файла и адреса электронной почты:
	   if (!AppUtil.checkEmail(email) || !archiveName.equals("report.zip"))
	   {
		   a_response.sendError(400, "Bad request");
		   return;
	   }
	   
	   List<File> files = AppUtil.getAllArchives(m_archivePath);
	   int size = files.size();
	   
	   /*Если количество архивов больше заданного,
	     происходит удаление файлов до установленного
	     количества:*/
	   if (size >= AppUtil.MAX_ARCHIVE_COUNT)
	   {
		   Collections.sort(files, new FileListSorter());
		   while (size >= AppUtil.MAX_ARCHIVE_COUNT)
		   {
			   files.get(size - 1).delete();
			   size--;
		   }
	   }
	   
	   String archivePath = m_archivePath + File.separator + email;
	   new File(archivePath).mkdir();
	   
	   archiveName = "report (" + AppUtil.getCurrentDateAndTime() + ").zip";
	   File archive = new File (archivePath + File.separator + archiveName);
	   archive.createNewFile(); //Создание пустого архива
	   
	   //Запись переданного файла в архив:
	   try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(archive)))
	   {
		   AppUtil.writeInputStreamToOutputStream(in, out, AppUtil.MAX_ARCHIVE_SIZE, true);
	   }
   }
}
