package report.actions.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.actions.comp.FileListSorter;
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
	   
	   try
	   {
		   while (zin.getNextEntry() != null)
		   {
			   entryCount++;
		   }
	   }
	   catch (ZipException e)
	   {
		   a_response.sendError(400, "Bad request");
		   return;
	   }
		   
	   if (entryCount > 100)
	   {
		   a_response.sendError(400, "Bad request");
		   return;
	   }
	   
	   String archivePath = m_archivePath + File.separator + email;
	   new File(archivePath).mkdir();
	   
	   List<File> files = AppUtil.getAllArchives(m_archivePath);
	   
	   if (files.size() == 20)
	   {
		   Collections.sort(files, new FileListSorter());
		   files.get(files.size() - 1).delete();
	   }
	   
	   fileName = "report (" + AppUtil.getCurrentDateAndTime() + ").zip";
	   File archive = new File (archivePath + File.separator + fileName);
	   archive.createNewFile();
	   
	   try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(archive)))
	   {
		   AppUtil.writeInputStreamToOutputStream(baseBin, out);
	   }
   }
}
