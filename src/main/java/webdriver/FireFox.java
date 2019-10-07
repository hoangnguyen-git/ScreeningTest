package webdriver;

import configure.Config;
import configure.Log;
import configure.PathConstant;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FireFox {
  protected Config config = Config.getInstance();
  private Log logger = Log.getInstance();
  private WebDriverManager driverManager = WebDriverManager.getInstance();

  public FirefoxOptions setFireFoxOptions() {
    try {
        logger.INFO("Start test with FireFox Browser:");

      // Get profile of default FireFox
      //            ProfilesIni profiles = new ProfilesIni();
      FirefoxProfile profile = new FirefoxProfile();

      // Set the browser language as English
        logger.INFO("Set English as language of FireFox Browser");
      profile.setPreference("intl.accept_languages", "en");

      // Set disabled notification in firefox
        logger.INFO("Set disabled notification in firefox");
      profile.setPreference("dom.webnotifications.enabled", false);

      // Handle SSL Certificate Error using Selenium Webdriver
        logger.INFO("Set accept SSL certificate for FireFox Browser");
      profile.setAcceptUntrustedCertificates(true);
      profile.setAssumeUntrustedCertificateIssuer(false);

      // Set download the file use firefox profile
        logger.INFO("Set download the file use firefox profile");

      // Reference link:
      // http://www.seleniumeasy.com/selenium-tutorials/how-to-download-a-file-with-webdriver
      profile.setPreference("browser.download.folderList", 2);
      profile.setPreference("browser.download.manager.showWhenStarting", false);
      profile.setPreference("browser.download.dir", PathConstant.fileDownloadPath);
      profile.setPreference(
          "browser.helperApps.neverAsk.openFile",
          "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
      profile.setPreference(
          "browser.helperApps.neverAsk.saveToDisk",
          "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");

      // Disable the built-in PDF viewer
      profile.setPreference("pdfjs.disabled", true);
      profile.setPreference("browser.helperApps.alwaysAsk.force", false);
      profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
      profile.setPreference("browser.download.manager.focusWhenStarting", false);
      profile.setPreference("browser.download.manager.useWindow", false);
      profile.setPreference("browser.download.manager.showAlertOnComplete", false);
      profile.setPreference("browser.download.manager.closeWhenDone", false);

      // Set Incognito/Private mode
      profile.setPreference("browser.private.browsing.autostart", true);

      FirefoxOptions options = new FirefoxOptions();
      options.setProfile(profile);
      if (config.getHeadLessMode()) {
          logger.INFO("Run in headless mode.");
        options.setHeadless(true);
      }

      return options;
    } catch (WebDriverException e) {
        logger.ERROR("Cannot start FireFox browser: \n" + e.toString());
      return null;
    }
  }
}
