package server.util;

import java.io.File;

public class UtilFile
{
  public static void removeDirectory(File folder)
  {
    if (!folder.exists()) {
      return;
    }
    File[] files = folder.listFiles();

    if (files != null)
    {
      for (File f : files)
      {
        if (f.isDirectory())
          removeDirectory(f);
        else {
          f.delete();
        }
      }
    }
    folder.delete();
  }

  public static void removeBaseDirectory(String folderName) {
    File curDir = new File(".");
    if(curDir != null && curDir.isDirectory()) {
      File[] fileList = curDir.listFiles();
      if (fileList != null && fileList.length > 0) {
        for (File file : fileList) {
          if (file != null && file.isDirectory()) {
            if (file.getName().equalsIgnoreCase(folderName)) {
              UtilFile.removeDirectory(file);
            }
          }
        }
      }
    }

  }


}