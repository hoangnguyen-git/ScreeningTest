package webdriver;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import configure.Config;
import configure.Log;
import configure.PathConstant;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.apache.commons.io.FileUtils;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.VideoFormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WebDriverManager {
  private static WebDriverManager driverManager;
  public WebDriver webDriver;
  protected Config config = Config.getInstance();
  private Log logger = Log.getInstance();
  private ScreenRecorder screenRecorder = null;
  private Set<Cookie> allCookies;

  public WebDriverManager() {}

  public static synchronized WebDriverManager getInstance() {
    if (driverManager == null) {
      driverManager = new WebDriverManager();
    }
    return driverManager;
  }

  public WebDriver getWebDriver() {
    return webDriver;
  }

  public Set<Cookie> getAllCookies() {
    return allCookies;
  }

  public void setAllCookies(WebDriver driver) {
    if (driver != null) {
      try {
        for (Cookie cookie : allCookies) {
          driver.manage().addCookie(cookie);
        }
      } catch (Exception e) {
          logger.ERROR(e.toString());
      }

    } else {
        logger.ERROR("driver is NULL");
    }
  }

  public void setupBrowser(WebDriver driver) {
    try {
      // Maximal browser screen
      driver.manage().window().maximize();

      // It sets the amount of time Selenium to wait for a page load to complete before throwing an
      // error.
      // If the timeout is negative, page loads can be indefinite.
      driver.manage().timeouts().pageLoadTimeout(config.getTimeOut(), TimeUnit.SECONDS);
      driver.manage().timeouts().setScriptTimeout(config.getTimeOut(), TimeUnit.SECONDS);
    } catch (WebDriverException e) {
        logger.ERROR("Unexpected exception while setting up browser: " + e.toString());
    }
  }

  public WebDriver setBrowserDriver() {
    try {
      BrowserType browserType = BrowserType.valueOf(config.getBrowserName().toUpperCase().trim());
      if (config.isDriverDownload()) {
          logger.INFO("Download web driver from Driver Manager.");
        switch (browserType) {
          case CHROME:
            ChromeDriverManager.chromedriver().forceCache().setup();
              logger.INFO(
                "Chrome version: " + ChromeDriverManager.chromedriver().getDownloadedVersion());
            Chrome chrome = new Chrome();
              webDriver = new ChromeDriver(chrome.setChromeOptions());
            break;
          case IE:
            InternetExplorerDriverManager.iedriver().setup();
            IE ie = new IE();
              webDriver = new InternetExplorerDriver(ie.setIEOptions());
              logger.INFO(
                "IE version: " + InternetExplorerDriverManager.iedriver().getDownloadedVersion());
            break;
          case FIREFOX:
            FirefoxDriverManager.firefoxdriver().setup();
            FireFox fireFox = new FireFox();
              webDriver = new FirefoxDriver(fireFox.setFireFoxOptions());
              logger.INFO(
                "FireFox version: " + FirefoxDriverManager.firefoxdriver().getDownloadedVersion());
            break;
          case SAFARI:
              webDriver = new SafariDriver();
            break;
          case EDGE:
            EdgeDriverManager.edgedriver().setup();
              webDriver = new EdgeDriver();
              logger.INFO("EDGE version: " + EdgeDriverManager.edgedriver().getDownloadedVersion());
            break;
          default:
            ChromeDriverManager.chromedriver().setup();
              logger.INFO(
                "Chrome version: " + ChromeDriverManager.chromedriver().getDownloadedVersion());
            Chrome chrome1 = new Chrome();
              webDriver = new ChromeDriver(chrome1.setChromeOptions());
            break;
        }
      } else {
          setWebDriverPath();
        switch (browserType) {
          case CHROME:
            Chrome chrome = new Chrome();
              webDriver = new ChromeDriver(chrome.setChromeOptions());
            break;
          case IE:
            IE ie = new IE();
              webDriver = new InternetExplorerDriver(ie.setIEOptions());
            break;
          case FIREFOX:
            FireFox fireFox = new FireFox();
              webDriver = new FirefoxDriver(fireFox.setFireFoxOptions());
            break;
          case SAFARI:
              webDriver = new SafariDriver();
            break;
          case EDGE:
              webDriver = new EdgeDriver();
            break;
          default:
            ChromeDriverManager.chromedriver().setup();
              logger.INFO("Chrome version: " + ChromeDriverManager.chromedriver().getVersions());
            Chrome chrome1 = new Chrome();
              webDriver = new ChromeDriver(chrome1.setChromeOptions());
            break;
        }
      }

    } catch (Exception e) {
        logger.ERROR("Unexpected exception while setting browser driver: " + e.toString());
      return null;
    }
    Capabilities caps = ((RemoteWebDriver) webDriver).getCapabilities();
      logger.INFO("Browser version: " + caps.getVersion() + " on " + caps.getPlatform());
    return webDriver;
  }

  public void closeBrowser(WebDriver driver) {
    if (null != driver) {
      driver.manage().deleteAllCookies();
      driver.quit();
    }
  }

  /**
   * Capture screenshot
   *
   * @param fileName file name of image.
   */
  public void captureScreenShot(String fileName) {
    try {
      // Used TakesScreenshot, OutputType Interface of selenium and File
      // class of java to capture screenshot of entire page.
      File src = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
      FileUtils.copyFile(src, new File(PathConstant.screenshotPath + fileName));
        logger.INFO("Capture screenshot: " + fileName);
    } catch (Exception e) {
        logger.ERROR("Error while capturing screenshot: " + e.toString());
    }
  }

  /**
   * Capture element screenshot
   *
   * @param webElement webElement
   * @param fileName file name of image.
   */
  public void captureElementScreenshot(WebElement webElement, String fileName) {
    try {
      // Used TakesScreenshot, OutputType Interface of selenium and File
      // class of java to capture screenshot of entire page.
      File src = ((TakesScreenshot) webElement).getScreenshotAs(OutputType.FILE);

      // Used selenium getSize() method to get height and width of
      // element.
      // Retrieve width of element.
      int elementWidth = webElement.getSize().getWidth();

      // Retrieve height of element.
      int elmentHeight = webElement.getSize().getHeight();

      // Used selenium Point class to get x y coordinates of Image
      // element.
      // Get location(x y coordinates) of the element.
      Point point = webElement.getLocation();
      int xcord = point.getX();
      int ycord = point.getY();

      // Reading full image screenshot.
      BufferedImage img = ImageIO.read(src);

      // Cut Image using height, width and x y coordinates parameters.
      BufferedImage dest = img.getSubimage(xcord, ycord, elementWidth, elmentHeight);
      ImageIO.write(dest, "jpeg", src);

      // Used FileUtils class of apache.commons.io.
      // Save Image screenshot in ScreenShot folder.
      FileUtils.copyFile(src, new File(PathConstant.screenshotPath + fileName));
    } catch (Exception e) {
        logger.ERROR("Error while capturing element screenshot: " + e.toString());
    }
  }

  /**
   * Capture element screenshot
   *
   * @param fileName file name of image.
   */
  public void captureFullPageScreenShot(String fileName) {
    try {
      Screenshot screenshot =
          new AShot()
              .shootingStrategy(ShootingStrategies.viewportPasting(4000))
              .takeScreenshot(webDriver);
      ImageIO.write(
          screenshot.getImage(), "jpg", new File(PathConstant.screenshotPath + fileName + ".jpg"));
        logger.INFO("Capture full screenshot into file: " + fileName + ".jpg");
    } catch (Exception e) {
        logger.ERROR("Failed to capture the full screenshot.");
    }
  }

  public void recordingScreen() {
    try {
      GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsConfiguration gc =
          GraphicsEnvironment.getLocalGraphicsEnvironment()
              .getDefaultScreenDevice()
              .getDefaultConfiguration();
      Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
      File file = new File(PathConstant.videoPath);
        screenRecorder =
          new ScreenRecorder(
              gc,
              captureSize,
              new Format(
                  FormatKeys.MediaTypeKey,
                  FormatKeys.MediaType.VIDEO,
                  FormatKeys.MimeTypeKey,
                  "video/avi"),
              new Format(
                  FormatKeys.MediaTypeKey,
                  FormatKeys.MediaType.VIDEO,
                  FormatKeys.EncodingKey,
                  "tscc",
                  VideoFormatKeys.CompressorNameKey,
                  "tscc",
                  VideoFormatKeys.DepthKey,
                  24,
                  FormatKeys.FrameRateKey,
                  Rational.valueOf(15.0),
                  VideoFormatKeys.QualityKey,
                  Float.valueOf(1.0f),
                  FormatKeys.KeyFrameIntervalKey,
                  900),
              new Format(
                  FormatKeys.MediaTypeKey,
                  FormatKeys.MediaType.VIDEO,
                  FormatKeys.EncodingKey,
                  "black",
                  FormatKeys.FrameRateKey,
                  Rational.valueOf(30.0)),
              null,
              file);
        screenRecorder.start();
    } catch (Exception e) {
        logger.ERROR("Error while recording video: " + e.toString());
    }
  }

  public void stopRecording() {
    if (null != screenRecorder) {
      try {
          screenRecorder.stop();

      } catch (IOException e) {
          logger.ERROR("Error while stopping recording video: " + e.toString());
      }
    }
  }

  /**
   * Read QR code from image
   *
   * @param qrCodeFileUrl file name of image.
   * @return QR code string.
   */
  public String readQRCode(String qrCodeFileUrl) {
    try {
      URL urlOfImage = new URL(qrCodeFileUrl);
      BufferedImage bufferedImage = ImageIO.read(urlOfImage);
      LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
      BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
      Result result = new MultiFormatReader().decode(binaryBitmap);
      return result.getText().trim();
    } catch (Exception e) {
      return "";
    }
  }

  /** Get the type of OS (Window or MAC) */
  public String getOSDetector() {
    String os = System.getProperty("os.name").toLowerCase();
    if (os.contains("win")) {
      return "WINDOW";
    } else if (os.contains("nux") || os.contains("nix")) {
      return "LINUX";
    } else if (os.contains("mac")) {
      return "MAC";
    } else {
      return "OTHER";
    }
  }

  public void setWebDriverPath() {
    try {
      BrowserType browserType = BrowserType.valueOf(config.getBrowserName().toUpperCase().trim());
      String osName = getOSDetector().trim().toUpperCase();
      String basePath = PathConstant.webDriverPath;
      switch (browserType) {
        case CHROME:
          if (osName.equals("WINDOW")) {
              logger.INFO("Set web driver path for Chrome Windows");
            System.setProperty(
                "webdriver.chrome.driver",
                basePath + "Window" + File.separator + "chromedriver.exe");
          } else if (osName.equals("MAC")) {
              logger.INFO("Set web driver path for Chrome MAC");
            System.setProperty(
                "webdriver.chrome.driver", basePath + "MacOS" + File.separator + "chromedriver");
          } else if (osName.equals("LINUX")) {
              logger.INFO("Set web driver path for Chrome LINUX");
            System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
          }
          break;
        case FIREFOX:
          if (osName.equals("WINDOW")) {
              logger.INFO("Set web driver path for Firefox Windows");
            System.setProperty(
                "webdriver.gecko.driver", basePath + "Window" + File.separator + "geckodriver.exe");
          } else if (osName.equals("MAC")) {
              logger.INFO("Set web driver path for Firefox MAC.");
            System.setProperty(
                "webdriver.gecko.driver", basePath + "MacOS" + File.separator + "geckodriver");
          } else if (osName.equals("LINUX")) {
              logger.INFO("Set web driver path for Firefox LINUX.");
            System.setProperty(
                "webdriver.gecko.driver", basePath + "Linux" + File.separator + "geckodriver");
          }
          break;
        case IE:
          if (osName.equals("WINDOW")) {
              logger.INFO("Set web driver path for IE Windows");
            System.setProperty(
                "webdriver.ie.driver", basePath + "Window" + File.separator + "IEDriverServer.exe");
          }
          break;
      }
    } catch (Exception e) {
        logger.ERROR("Failed to set web driver path\n" + e.toString());
    }
  }

  public enum BrowserType {
    IE,
    CHROME,
    FIREFOX,
    SAFARI,
    EDGE
  }
}
