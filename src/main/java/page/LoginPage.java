package page;

import helper.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    public LoginPage(WebDriver driver) {
        super(driver);
        driver.get(config.getWebEnvironment().get("url"));
    }

    @FindBy(id = "identifierId")
    private WebElement email;

    @FindBy(css = "input[type='password']")
    private WebElement password;

    @FindBy(xpath = "//span[text()='Next']")
    private WebElement nextButton;

    @FindBy(className = "TquXA")
    private WebElement selectLanguage;

    @FindBy(xpath = "(//span[contains(text(),'English (United Kingdom)')])[last()]")
    private WebElement englishLanguage;

    public void enterEmail(String email) {
        logger.INFO("Enter email:" + email );
        commonActions.sendKeys(this.email, email);
    }

    public void enterPassword(String password) {
        logger.INFO("Enter password:" + password );
        commonActions.sendKeys(this.password, password);
    }

    public void clickOnNextButton() {
        logger.INFO("Click on Next button");
        commonActions.click(this.nextButton);
    }

    public void setEnglishLanguage() {
        logger.INFO("Click on drop down list");
        commonActions.clickByJavaScript(this.selectLanguage);
        logger.INFO("Click on English language");
        commonActions.clickByJavaScript(this.englishLanguage);
        logger.INFO("Wait for changing language on the page");
        WaitUtils.sleep(2);
    }
}
