package report.actions.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.ZipInputStream;
 
/**
 * Утилитарный класс.
 */
public class AppUtil 
{
	public static final int MAX_ARCHIVE_COUNT = 20; 
	public static final int MAX_ENTRY_COUNT = 100; 
	
	private static final long m_kByte = 1024;

	public static final long MAX_ARCHIVE_SIZE = 5 * m_kByte * m_kByte; 
	public static final long MAX_UNCOMPRESSED_ENTRY_SIZE = 10 * m_kByte * m_kByte * m_kByte; 
	public static final double MAX_COMPRESSION_RATIO = 100;
	
    public static boolean writeInputStreamToOutputStream (InputStream a_in, OutputStream a_out, 
    		                                              long a_byteNumber) throws IOException
    {
       long byteCount = 0;
       
       byte[] buffer = new byte[1024*64];
       int length;
 	   while ((length = a_in.read(buffer)) > -1)
 	   {
 		   byteCount += length;
 		   if (byteCount > a_byteNumber)
		   {
			   return false;
		   }
 		   a_out.write(buffer, 0, length);
 	   }
 	   a_out.flush();
 	   return true;
    }
     
    /**
     * Читает следующее вложение архива и закрывает его.
     * Метод должен быть использован после вызова метода
     * ZipInputStream.getNextEntry().
     * @param a_zin
     * 		  Входной поток архива
     * @param a_byteNumber
     * 		  Количество байтов для чтения
     * @return если количество прочитанных байтов 
     * 		   больше a_byteNumber - false, иначе - true
     * 		   (имеется незначительная неточность в подсчёте
     * 		   считываемых байтов; она связана с размером
     * 		   буфера для чтения (64 КБ)).
     * @throws IOException
     */
    public static boolean readNextZipEntry (ZipInputStream a_zin, long a_byteNumber) throws IOException
    {
    	long byteCount = 0;
        
        byte[] buffer = new byte[1024*64];
        int length;
        while ((length = a_zin.read(buffer)) > -1)
   	    {
   		    byteCount += length;
   		    if (byteCount > a_byteNumber) 
		    {    
			    return false;
		    }
   	    }
   	    a_zin.closeEntry();
   	    return true;
    }
    
    /**
     * Читает длину следующей строки из входного потока,
     * затем читает следующую строку прочитанной длины и
     * возвращает её.
     * @param a_in
     * 		  Входной поток
     * @return строку из входного потока
     * @throws IOException
     */
    public static String getStringFromInputStream (InputStream a_in) throws IOException
    {
    	String stringLengthLine = AppUtil.getNextStringFromInputStream(a_in, 2);
	    int stringLength = Integer.parseInt(stringLengthLine);
	    
		return AppUtil.getNextStringFromInputStream(a_in, stringLength);
    }
    
    private static String getNextStringFromInputStream (InputStream a_in, int a_stringLength) throws IOException
    {
    	String result = "";
    	for (int j = 0; j < a_stringLength; j++)
		{
    		result += Character.toString((char)a_in.read());
		}
    	return result;
    }
    
    /**
     * @return путь к папке, в которой находятся папки 
     * с архивами (содержит путь ко временной директории
     * пользователя)
     */
    public static String getReportArchivePath ()
    {
    	String path = System.getProperty("java.io.tmpdir") + File.separator + "reports";
    	File archiveDir = new File(path);
    	archiveDir.mkdir();
    	return path;
    }
    
    /**
     * @param a_line
     * 		  Строка
     * @param a_c
     * 	      Символ строки
     * @return подстроку строки a_line (от символа на позиции
     * 0 до символа a_c (не включая его)). Если символа a_c нет
     * в строке, возвращает саму строку
     */
    public static String getSubstringToCharacter (String a_line, char a_c)
    {
    	int end = a_line.indexOf(a_c);
		if (end != -1) a_line = a_line.substring(0, end);
		
		return a_line;
    }
    
    public static String getCurrentDateAndTime ()
	{
		Calendar calendar = GregorianCalendar.getInstance();
		
		String day = addZeroToString((Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))));
		String month =  addZeroToString(Integer.toString(calendar.get(Calendar.MONTH) + 1));
		String year = Integer.toString(calendar.get(Calendar.YEAR));
		
		String hour =  addZeroToString(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));
		String min =  addZeroToString(Integer.toString(calendar.get(Calendar.MINUTE)));
		String sec =  addZeroToString(Integer.toString(calendar.get(Calendar.SECOND)));
		
		return day + "-" + month + "-" + year + " - " + hour + "-" + min + "-" + sec;
	}
    
    /**
	 * Устанавливает началом строки a_string символ "0", если длина строки равна 1.
	 * @param a_string 
	 *        Строка для изменения
	 * @return изменённую строку
	 */
    public static String addZeroToString (String a_string)
	{
		return a_string.length() == 1 ? "0" + a_string : a_string;
	}
    
    /**
	 * Проверяет переданную строку на соответствие формату адреса электронной почты.
	 * @param a_email 
	 *        Строка для проверки
	 * @return true - если строка прошла проверку,
	 * false - иначе
	 */
    public static boolean checkEmail (String a_email)
	{	
		if (a_email == null) return false;
		
		if (a_email.contains(File.separator) || a_email.contains("/") || a_email.contains("\\") ||
			a_email.contains(":") || a_email.contains("*") || a_email.contains("?") ||
			a_email.contains("\"") || a_email.contains("<") || a_email.contains(">") || a_email.contains("|")) 
		{
			return false;
		}
		
		int atCharIndex = a_email.indexOf('@');
		if (atCharIndex == -1 || atCharIndex == 0 || atCharIndex >= (a_email.length() - 3)) return false;
		
		int dotCharCount = 0;
		for (int i = atCharIndex + 1; i < a_email.length(); i++)
		{
			if (a_email.charAt(i) == '.') dotCharCount++;
		}
		if (dotCharCount != 1) return false;
		
		int dotCharIndex = a_email.lastIndexOf('.');
		if (dotCharIndex == (atCharIndex + 1) || dotCharIndex == (a_email.length() - 1)) return false;
		
		return true;
	}
    
    /**
     * @param a_archivePath
     * 		  Путь к папке, которая содержит 
     *        папки с архивами
     * @return все архивы во всех папках в директории
     * a_archivePath
     */
    public static List<File> getAllArchives (String a_archivePath)
    {
    	File archiveDir = new File(a_archivePath);
 	   
 	    File[] dirs = archiveDir.listFiles();
 	    List<File> files = new ArrayList<>();
 	   
 	    for (File d : dirs)
 	    {
 		    if (d.isDirectory())
 		    {
 			    for (File f : d.listFiles())
 			    {
 				    files.add(f);
 			    }
 		    }
 	    }
 	    
 	    return files;
    }
}