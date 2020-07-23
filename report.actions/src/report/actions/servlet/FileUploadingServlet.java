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
import java.util.zip.ZipEntry;
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
   
   private String m_email = "";
   private String m_archiveName = "";
   
   private String m_archivePath = "";
 
   public FileUploadingServlet () throws IOException 
   {
      super();
      m_archivePath = AppUtil.getReportArchivePath();
   }
   
   @Override
   protected void doPost (HttpServletRequest a_request, HttpServletResponse a_response) throws ServletException, IOException, NumberFormatException 
   {
	   ByteArrayInputStream in = checkRequestAndGetInputStream(a_request);
	   
	   if (in == null)
	   {
		   a_response.sendError(400, "Bad request");
		   return;
	   }
	   
	   List<File> files = AppUtil.getAllArchives(m_archivePath);
	   int size = files.size();
	   if (size >= AppUtil.MAX_ARCHIVE_COUNT)
	   {
		   Collections.sort(files, new FileListSorter());
		   while (size >= AppUtil.MAX_ARCHIVE_COUNT)
		   {
			   files.get(size - 1).delete();
			   size--;
		   }
	   }
	     
	   String archivePath = m_archivePath + File.separator + m_email;
	   new File(archivePath).mkdir();
	   
	   m_archiveName = "report (" + AppUtil.getCurrentDateAndTime() + ").zip";
	   File archive = new File (archivePath + File.separator + m_archiveName);
	   archive.createNewFile();
	   
	   try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(archive)))
	   {
		   AppUtil.writeInputStreamToOutputStream(in, out);
	   }
   }
   
   private ByteArrayInputStream checkRequestAndGetInputStream (HttpServletRequest a_request) throws IOException
   {
	   ByteArrayOutputStream out = new ByteArrayOutputStream();
	   if (!AppUtil.writeInputStreamToOutputStream(a_request.getInputStream(), out))
	   {
		   return null;
	   }
	   
	   ByteArrayInputStream baseIn = new ByteArrayInputStream(out.toByteArray());
	   
	   try (ByteArrayInputStream inForZipChecking = new ByteArrayInputStream(out.toByteArray()))
	   {  
		   String email = AppUtil.getStringFromInputStream(baseIn);
		   AppUtil.getStringFromInputStream(inForZipChecking);
		   
		   String archiveName = AppUtil.getStringFromInputStream(baseIn);
		   AppUtil.getStringFromInputStream(inForZipChecking);
		   
		   if (!AppUtil.checkEmail(email) || !archiveName.equals("report.zip"))
		   {
			   return null;
		   }
		   
		   m_email = email;
		   m_archiveName = archiveName;
		   
		   int entryCount = 0;
		   try (ZipInputStream zin = new ZipInputStream(new BufferedInputStream (inForZipChecking)))
		   {
			   try
			   {
				   ZipEntry entry = zin.getNextEntry();
				   while (entry != null)
				   {  
					   if (!AppUtil.readNextZipEntry(zin)) return null;
					   
					   long compSize = entry.getCompressedSize();
					   long uncompSize = entry.getSize();
					   if ((double)uncompSize/compSize > AppUtil.MAX_COMPRESSION_RATIO)
					   {  
						   return null;
					   }
					   entryCount++;
					   entry = zin.getNextEntry();
				   }
			   }
			   catch (ZipException e)
			   {
				  return null;
			   }
		   }
		   
		   if (entryCount > AppUtil.MAX_ENTRY_COUNT)
		   {
			   return null;
		   }
	   }
	   return baseIn;
   }
}
