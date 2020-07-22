package report.actions.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	   
	   String fileName = AppUtil.getStringFromInputStream(baseBin);
	   AppUtil.getStringFromInputStream(binForZipChecking);
	   
	   if (!checkRequest(email, fileName, baseBin, binForZipChecking))
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
   
   private boolean checkRequest (String a_email, String a_fileName, InputStream a_baseBin, InputStream a_binForZipChecking) throws IOException
   {
	   if (!AppUtil.checkEmail(a_email) || !a_fileName.equals("report.zip") || a_baseBin.available() > AppUtil.MAX_ARCHIVE_SIZE)
	   {
		   return false;
	   }
	   
	   ZipInputStream zin = new ZipInputStream(new BufferedInputStream (a_binForZipChecking));
	   int entryCount = 0;
	   try
	   {
		   ZipEntry entry = zin.getNextEntry();
		   while (entry != null)
		   {
			   String name = entry.getName();
			   if (name.endsWith(".zip")) return false;
			   
			   if (!AppUtil.readNextZipEntry(zin)) return false;
			   
			   long compSize = entry.getCompressedSize();
			   long uncompSize = entry.getSize();
			   if (uncompSize > AppUtil.MAX_UNCOMPRESSED_ENTRY_SIZE || (double)uncompSize/compSize > AppUtil.MAX_COMPRESSION_RATIO)
			   {
				   zin.close();
				   return false;
			   }
			   entryCount++;
			   entry = zin.getNextEntry();
		   }
	   }
	   catch (ZipException e)
	   {
		  return false;
	   }
	   
	   if (entryCount > AppUtil.MAX_ENTRY_COUNT)
	   {
		   return false;
	   }
	   return true;
   }
}
