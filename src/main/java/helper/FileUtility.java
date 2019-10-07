package helper;

import configure.Log;
import configure.PathConstant;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class FileUtility {
  private static Log logger = Log.getInstance();

  public static Properties getConfigFile(String fullPath) {
    Properties prop = new Properties();
    InputStream input = null;

    try {
      input = new FileInputStream(fullPath);
      prop.load(input);
    } catch (Exception ex) {
      logger.ERROR(ex.toString());
    } finally {
      try {
        input.close();
      } catch (IOException ex) {
        logger.ERROR(ex.toString());
      }
    }
    return prop;
  }

  /**
   * Replace in file from string from to char to
   *
   * @param filePath path of file to replace
   * @param from from this String to replace
   * @param to to this char to replace
   * @param replace value replace
   */
  public static void replaceInFile(String filePath, String from, String to, String replace) {
    Path path = Paths.get(filePath);
    Charset charset = StandardCharsets.UTF_8;
    try {
      // Read file to String
      String contentFile = new String(Files.readAllBytes(path), charset);

      // Get index to begin replace
      int index = contentFile.indexOf(from) + from.length();
      String content = "";

      // Find context need to replace
      for (int i = index; i <= contentFile.length() - 1; i++) {
        content = content + String.valueOf(contentFile.charAt(i));
        if (String.valueOf(contentFile.charAt(i + 1)).equals(to)) {
          break;
        }
      }

      // Replace context with value
      contentFile = contentFile.replaceAll(content, replace);

      // Write file again
      Files.write(path, contentFile.getBytes(charset));
    } catch (IOException e) {
      System.out.println("replaceInFile: " + e.toString());
      logger.ERROR(e.toString());
    }
  }

  /**
   * Create folder if not exist
   *
   * @param destination path of folder
   */
  public static void createFolder(String destination) {
    destination = System.getProperty("user.dir") + destination;
    try {
      File file = new File(destination);

      // Check folder exist, if not exist, create folder
      if (!file.exists()) {
        if (file.mkdir()) {
          System.out.println("Directory is created: " + destination);
        } else {
          System.out.println("Failed to create directory: " + destination);
        }
      } else {
        System.out.println("Directory is existed: " + destination);
      }
    } catch (Exception e) {
      System.out.println("CreateFolder Exception: " + e.toString());
    }
  }

  public static void deleteAllFilesInFolder(String path) {
    try {
      path = System.getProperty("user.dir") + path;
      File file = new File(path);
      if (file.exists()) {
        for (File deleteFile : file.listFiles()) {
          deleteFile.delete();
        }
      }
    } catch (Exception e) {
      System.out.println("Exception: " + e.toString());
    }
  }

  public static void deleteFileName(String filePath) {
    filePath = System.getProperty("user.dir") + filePath;
    File file = new File(filePath);
    if (file.exists()) {
      file.delete();
    }
  }

  public static File getResourceFile(String fileName) throws IOException {
    if (FileUtility.class.getClassLoader().getResourceAsStream(fileName) != null) {
      InputStream resourceAsStream =
          FileUtility.class.getClassLoader().getResourceAsStream(fileName);
      File file = new File(fileName, "");
      FileUtils.copyInputStreamToFile(resourceAsStream, file);
      return file;
    } else {
      return new File(fileName);
    }
  }

  /**
   * Count the numer of file in folfer
   *
   * @param filePath path of folder
   * @return fileCount the number of file in folder.
   */
  public static int countFile(String filePath) {
    int fileCount = 0;
    try {
      File directory = new File(filePath);
      fileCount = directory.list().length;
    } catch (Exception e) {
      logger.ERROR("Error while counting the number of file in folder: " + e.toString());
    }
    return fileCount;
  }

  public static boolean isFileDownLoad(String fileName) {
    String downloadPath =
        System.getProperty("user.dir") + File.separator + "FileDownLoad" + File.separator;
    boolean flag = false;
    File dir = new File(downloadPath);
    File[] files = dir.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].getName().equals(fileName)) {
        flag = true;
      }
    }
    return flag;
  }

  /**
   * Get the latest file from a specific directory.
   *
   * @return latest modified file.
   */
  public static File getLatestFileDownLoad() {
    String downloadPath = PathConstant.fileDownloadPath;
    File dir = new File(downloadPath);
    File[] files = dir.listFiles();
    if (files == null || files.length == 0) {
      return null;
    } else {
      File lastModifiedFile = files[0];
      for (int i = 0; i < files.length; i++) {
        if (lastModifiedFile.lastModified() < files[i].lastModified()) {
          lastModifiedFile = files[i];
        }
      }
      return lastModifiedFile;
    }
  }

  /**
   * Get file extension
   *
   * @param fileName filepath
   * @return fileExtension
   */
  public static String getFileExtension(String fileName) {
    if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
      return fileName.substring(fileName.lastIndexOf(".") + 1).trim().toLowerCase();
    } else {
      return "";
    }
  }

  /**
   * Get size of file.
   *
   * @param path Path to file.
   * @return Size of file in kB.
   */
  public static long getFileSize(String path) {
    File file;
    long size = 0;
    file = new File(path);
    if (file.exists()) {
      size = file.length() / 1024; // In KBs
    } else {
      logger.ERROR("File is not found");
    }
    return size;
  }

  /**
   * Read content of file.
   *
   * @param filePath File path as String.
   * @return Content of file as String.
   * @throws IOException When fail to read file.
   */
  public static String readFile(String filePath) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(filePath));
    return new String(encoded, Charset.defaultCharset());
  }
}
