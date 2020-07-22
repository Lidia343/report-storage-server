package report.actions.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.util.AppUtil;
 
@WebServlet("/file/upload")
public class FileUploadingServlet extends HttpServlet 
{
   private static final long serialVersionUID = 1L;
   private String m_archivePath = "";
 
   public FileUploadingServlet () throws IOException 
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
	   ByteArrayOutputStream bout = new ByteArrayOutputStream();
	   AppUtil.writeInputStreamToOutputStream(a_request.getInputStream(), bout);
	   
	   ByteArrayInputStream baseBin = new ByteArrayInputStream(bout.toByteArray());
	   ByteArrayInputStream binForZipChecking = new ByteArrayInputStream(bout.toByteArray());
	   
	   String email = AppUtil.getStringFromInputStream(baseBin);
	   AppUtil.getStringFromInputStream(binForZipChecking);
	   
	   if (!AppUtil.checkEmail(email))
	   {
		   a_response.sendError(400, "Bad request");
		   return;
	   }
	   
	   String fileName = AppUtil.getStringFromInputStream(baseBin);
	   AppUtil.getStringFromInputStream(binForZipChecking);
	   
	   if (!fileName.equals("report.zip"))
	   {
		   a_response.sendError(400, "Bad request");
		   return;
	   }
	   
	   ZipInputStream zin = new ZipInputStream(new BufferedInputStream (binForZipChecking));
	   int entryCount = 0;
	   ZipEntry entry = zin.getNextEntry();
	   while (entry != null)
	   {
		   entryCount++;
		   String name = entry.getName();
	       if (!name.equals(".log") && !name.equals("metadata.xml") && !name.equals("summary.txt"))
		   {
			   a_response.sendError(400, "Bad request");
			   return;
		   }
		   entry = zin.getNextEntry();
	   }
		   
	   if (entryCount < 2 || entryCount > 3)
	   {
		   a_response.sendError(400, "Bad request");
		   return;
	   }
	   
	   String archivePath = m_archivePath + File.separator + email;
	   new File(archivePath).mkdir();
	   
	   fileName = "report (" + AppUtil.getCurrentDateAndTime() + ").zip";
	   File archive = new File (archivePath + File.separator + fileName);
	   archive.createNewFile();
	   
	   try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(archive)))
	   {
		   AppUtil.writeInputStreamToOutputStream(baseBin, out);
	   }
   }
}
