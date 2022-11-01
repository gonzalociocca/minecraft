package mineultra.core.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtil
{
    List<String> fileList;
 
    public static Boolean UnzipToDirectory(String zipFile, String extractFolder){
    try
    {
        int BUFFER = 2048;
        File file = new File(zipFile);

        ZipFile zip = new ZipFile(file);
        String newPath = extractFolder;

        new File(newPath).mkdir();
        Enumeration zipFileEntries = zip.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements())
        {
            // grab a zip file entry
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();

            File destFile = new File(newPath, currentEntry);
            //destFile = new File(newPath, destFile.getName());
            File destinationParent = destFile.getParentFile();

            // create the parent directory structure if needed
            destinationParent.mkdirs();

            if (!entry.isDirectory())
            {
                BufferedInputStream is = new BufferedInputStream(zip
                .getInputStream(entry));
                int currentByte;
                // establish buffer for writing file
                byte data[] = new byte[BUFFER];

                // write the current file to disk
                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos,
                BUFFER);

                // read and write until last byte is encountered
                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }


        }
        while(!zipFileEntries.hasMoreElements()){
        return true;            
        }

        
    }
    catch (Exception e) 
    {
        System.out.println("ERROR: "+e.getMessage());
    }

return false;
   }    

/*
  public static void UnzipToDirectory(String zipFilePath, String outputDirectory)
  {
    FileInputStream fileInputStream = null;
    ZipInputStream zipInputStream = null;
    FileOutputStream fileOutputStream = null;
    BufferedOutputStream bufferedOutputStream = null;
    BufferedInputStream bufferedInputStream = null;
    try
    {
      fileInputStream = new FileInputStream(zipFilePath);
      bufferedInputStream = new BufferedInputStream(fileInputStream);
      zipInputStream = new ZipInputStream(bufferedInputStream);
      ZipEntry entry;
      while ((entry = zipInputStream.getNextEntry()) != null)
      {

        byte[] buffer = new byte[2048];

        fileOutputStream = new FileOutputStream(outputDirectory + "/" + entry.getName());
        bufferedOutputStream = new BufferedOutputStream(fileOutputStream, buffer.length);
        int size;
        while ((size = zipInputStream.read(buffer, 0, buffer.length)) != -1)
        {

          bufferedOutputStream.write(buffer, 0, size);
        }

        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
      }

      zipInputStream.close();
      bufferedInputStream.close();
      fileInputStream.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();

      if (fileInputStream != null)
      {
        try
        {
          fileInputStream.close();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }

      if (bufferedInputStream != null)
      {
        try
        {
          bufferedInputStream.close();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }

      if (zipInputStream != null)
      {
        try
        {
          zipInputStream.close();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }

      if (fileOutputStream != null)
      {
        try
        {
          fileOutputStream.close();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }

      if (bufferedOutputStream != null)
      {
        try
        {
          bufferedOutputStream.close();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }
    }
  }*/
}