package configure;

import java.io.File;

public final class PathConstant {
  public static final String homePath = System.getProperty("user.dir") + File.separator;
  public static final String logPath = homePath + "Log" + File.separator;
  public static final String reportPath = homePath + "Report" + File.separator;
  public static final String screenshotPath = homePath + "ScreenShot" + File.separator;
  public static final String fileDownloadPath = homePath + "FileDownLoad" + File.separator;
  public static final String fileUploadPath = homePath + "FileUpload" + File.separator;
  public static final String webDriverPath = homePath + "WebDriver" + File.separator;
  public static final String videoPath = homePath + "Video" + File.separator;
    public static final String configPath = homePath + "src" + File.separator + "main" + File.separator + "resources"
          + File.separator
          + "config"
          + File.separator;
    public static final String dataTestPath = homePath + "src" + File.separator + "main" + File.separator + "resources"
          + File.separator
          + "DataTest"
          + File.separator;
    public static final String mobileAppPath = homePath + "src" + File.separator + "main" + File.separator + "resources"
          + File.separator
          + "App"
          + File.separator;
}
