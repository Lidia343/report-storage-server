package jetty.reports.actions.comp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;

/**
 * Компаратор для сортировки файлов по дате создания 
 * (располагает новые файлы в начале списка).
 */
public class FileListSorter implements Comparator<File> 
{
	/**
	 * Сравнивает время создания первого и второго файлов.
	 */
	@Override
	public int compare(File a_f1, File a_f2) 
	{
		try 
		{
			BasicFileAttributes attr1 = Files.readAttributes(((File)a_f1).toPath(), BasicFileAttributes.class);
			BasicFileAttributes attr2 = Files.readAttributes(((File)a_f2).toPath(), BasicFileAttributes.class);
			
			return attr2.creationTime().compareTo(attr1.creationTime());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return 0;
		}
	}
}
