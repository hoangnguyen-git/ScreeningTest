package webdriver;

import configure.Log;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class IE {
    private Log logger = Log.getInstance();
    private WebDriverManager driverManager = WebDriverManager.getInstance();

    public InternetExplorerOptions setIEOptions() {
        try {
            this.logger.INFO("Start test with IE Browser:");
            DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
            InternetExplorerOptions IEoptions = new InternetExplorerOptions();
            // Set capability for fix bug input text slow.
            IEoptions.setCapability(
                    InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            IEoptions.setCapability("requireWindowFocus", true);

            // Make sure that session is closed.
            IEoptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);

            // SSL Certificate Error Handling in IE
            IEoptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            return IEoptions;

        } catch (WebDriverException e) {
            this.logger.ERROR("Cannot start IE browser:\n " + e.toString());
            return null;
        }
    }
}
