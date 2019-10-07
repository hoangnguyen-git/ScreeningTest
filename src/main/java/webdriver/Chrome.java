package webdriver;

import configure.Config;
import configure.Log;
import configure.PathConstant;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.util.HashMap;
import java.util.Map;

public class Chrome {
  protected Config config = Config.getInstance();
  private Log logger = Log.getInstance();
  private WebDriverManager driverManager = WebDriverManager.getInstance();

  /*
   *
   */
  public ChromeOptions setChromeOptions() throws Exception {
    try {
      // Set Chrome driver
        logger.INFO("Start test with Chrome Browser:");
      // Set option for Chrome browser
      ChromeOptions options = new ChromeOptions();
      options.addArguments("chrome.switches", "--disable-extensions");
      options.addArguments("--test-type");
      // options.addArguments("start-maximized");
      options.addArguments("test-type");
      options.addArguments("--js-flags=--expose-gc");
      options.addArguments("--enable-precise-memory-info");
      options.addArguments("--disable-popup-blocking");
      options.addArguments("--disable-default-apps");
      options.addArguments("test-type=browser");
      options.addArguments("disable-infobars");
      options.addArguments("--disable-extensions");
      options.addArguments("no-sandbox");

      // Set Incognito/Private mode
//      options.addArguments("--incognito");

      // Set the browser language as English
        logger.INFO("Set English as language of Chrome Browser");
      options.addArguments("–lang=en");

      // Create object of HashMap Class
      Map<String, Object> prefs = new HashMap<String, Object>();

      // Disable save password dialog.
      prefs.put("credentials_enable_service", false);
      prefs.put("profile.password_manager_enabled", false);

      // Set the notification setting it will override the default setting
      prefs.put("profile.default_content_setting_values.notifications", 2);

      // Set download file
      prefs.put("profile.default_content_settings.popups", 0);
      prefs.put("download.default_directory", PathConstant.fileDownloadPath);

      // Set the experimental option
      options.setExperimentalOption("prefs", prefs);

      // Set accept SSL
      options.setCapability(ChromeOptions.CAPABILITY, prefs);
      options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
      options.setCapability(ChromeOptions.CAPABILITY, options);
      options.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);

      // Run selenium test in headless mode with real Chrome
      // Overcome the issue on Jenskin Linux Ubuntu 16.x
      if (config.getHeadLessMode()) {
          logger.INFO("Run in headless mode");
        options.setHeadless(true);
        if (driverManager.getOSDetector().equals("LINUX")) {
            logger.INFO("Set browser config: --disable-gpu,--disable-dev-shm-usage ");
          options.addArguments("--disable-gpu");
          options.addArguments("--disable-dev-shm-usage");
          options.addArguments("--window-size=1920,1080");
        } else if (driverManager.getOSDetector().equals("WINDOW")) {
            logger.INFO("Set browser size: --window-size=1920,1080");
          options.addArguments("--window-size=1920,1080");
        }

        //                options.addArguments("--window-size=1920,1080");
      } else {
        options.addArguments("start-maximized");
      }
      return options;
    } catch (WebDriverException e) {
        logger.ERROR("Cannot start Chrome browser: \n" + e.toString());
      return null;
    }
  }
}
