package page;

//import Utils.DataGenerate;
import configure.Config;
import configure.Log;
import helper.CommonActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {
    public WebDriver driver;
    public CommonActions commonActions;
//    public DataGenerate dataGenerate = new DataGenerate();
    protected Config config = Config.getInstance();
    protected Log logger = Log.getInstance();

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        commonActions = new CommonActions(driver);
    }

    public String getUrlENV() {
        return config.getWebEnvironment().get("url");
    }

    public String getUsernameENV() {
        return config.getWebEnvironment().get("username");
    }

    public String getPasswordENV() {
        return config.getWebEnvironment().get("password");
    }

}
