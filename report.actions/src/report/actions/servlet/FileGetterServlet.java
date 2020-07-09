package report.actions.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.RequestDispatcher;
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
 
   public FileGetterServlet() throws IOException 
   {
      super();
      m_archivePath = AppUtil.getReportArchivePath();
   }
   
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
   {
	   String jspPath = "/WEB-INF/views/existingFileView.jsp";
	   File archiveDir = new File(m_archivePath);
	   if (archiveDir.listFiles().length == 0) jspPath = "/WEB-INF/views/nonExistingFileView.jsp";
       RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(jspPath);
       dispatcher.forward(request, response);
   }
 
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException 
   {
	   InputStream requestIn = request.getInputStream();
	   
	   String fileNameLengthLine = AppUtil.getNextStringFromInputStream(requestIn, m_lengthOfSizeLine);
	   int fileNameLength = Integer.parseInt(fileNameLengthLine);
	   
	   String fileName = AppUtil.getNextStringFromInputStream(requestIn, fileNameLength);
	  
	   String entryCountLine = AppUtil.getNextStringFromInputStream(requestIn, m_lengthOfSizeLine);
	   int entryCount = Integer.parseInt(entryCountLine);
	   
	   File archive = new File (m_archivePath + "\\" + fileName);
	   archive.createNewFile();
	   try (BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream(archive));
	        ZipOutputStream zout = new ZipOutputStream (bufOut))
	   {
		   for(int i = 0 ; i < entryCount; i++)
		   {
			   String entryNameLengthLine = AppUtil.getNextStringFromInputStream(requestIn, m_lengthOfSizeLine);
			   int entryNameLength = Integer.parseInt(entryNameLengthLine);
			   
			   String entryName = AppUtil.getNextStringFromInputStream(requestIn, entryNameLength);
			   zout.putNextEntry(new ZipEntry(entryName));
			   
			   String entrySizeLengthLine = AppUtil.getNextStringFromInputStream(requestIn, m_lengthOfSizeLine);
			   int entrySizeLength = Integer.parseInt(entrySizeLengthLine);
			   
			   String entrySizeLine = AppUtil.getNextStringFromInputStream(requestIn, entrySizeLength);
			   long entrySize = Long.parseLong(entrySizeLine);
			   
			   for (long j = 0L; j < entrySize; j++)
			   {
				   zout.write(requestIn.read());
			   }
			   zout.closeEntry(); 
		   }
	   }
   }
}
