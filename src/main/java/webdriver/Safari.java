package webdriver;

import configure.Log;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class Safari {
  private Log logger = Log.getInstance();
  private WebDriver driver;
  private WebDriverManager driverManager = WebDriverManager.getInstance();

  public Safari() throws Exception {
    this.logger.INFO("Start test with Safari Browser:");
    SafariOptions options = new SafariOptions();

    // Set Safari to forget session everytime.
    // options.setUseCleanSession(true);

    Capabilities capabilities = DesiredCapabilities.safari();
    try {
      this.driver = new SafariDriver(capabilities);
      this.driverManager.setupBrowser(this.driver);
    } catch (WebDriverException e) {
      this.logger.ERROR("Cannot start Safari broswer: " + e.toString());
    }
    // Set up for FireFox browser.
    this.driverManager.setupBrowser(this.driver);
  }

  public WebDriver getDriver() {
    return this.driver;
  }
}
