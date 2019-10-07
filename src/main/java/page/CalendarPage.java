package page;

import configure.Config;
import helper.LocatorType;
import helper.WaitUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CalendarPage extends BasePage {
    public CalendarPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//div[text()='Create']")
    private WebElement createButton;

    @FindBy(css = "input[aria-label = 'Add title']")
    private WebElement eventTitle;

    @FindBy(css = "input[aria-label = 'Title']")
    private WebElement title;

    @FindBy(xpath = "//span[text() = 'Event']")
    private WebElement eventOption;

    @FindBy(css = "input[aria-label = 'Start date']")
    private WebElement startDate;

    @FindBy(css = "input[aria-label = 'End date']")
    private WebElement endDate;

    @FindBy(css = "input[aria-label = 'Start time']")
    private WebElement startTime;

    @FindBy(css = "input[aria-label = 'End time']")
    private WebElement endTime;

    @FindBy(css = "input[aria-label = 'Guests']")
    private WebElement guest;

    @FindBy(xpath = "//span[text() = 'Add location or conferencing']")
    private WebElement locationButton;

    @FindBy(xpath = "//span[text() = 'Add location']")
    private WebElement addLocation;

    @FindBy(css = "input[aria-label = 'Location']")
    private WebElement location;

    @FindBy(xpath = "//span[text() = 'Add description']")
    private WebElement addDescription;

    @FindBy(xpath = "//div[@aria-label = 'Description']")
    private WebElement descriptions;

    @FindBy(xpath = "//span[text() = 'More options']")
    private WebElement moreOption;

    @FindBy(xpath = "//span[text() = 'Save']")
    private WebElement saveButton;

    @FindBy(className = "FfrJD")
    private WebElement popUpConfirmation;

    @FindBy(xpath = "//span[contains(text(),'t send')]")
    private WebElement dontSendButton;

    @FindBy(className = "rSoRzd")
    private WebElement monthYear;

    @FindBy(css = "div.MmhHI.KSxb4d")
    private WebElement date;

    @FindBy(css = "div.Jmftzc.gVNoLb.EiZ8Dd")
    private WebElement time;


    public void waitForPageIsDisplayed() {
        logger.INFO("Wait for page is loaded successfully");
        WaitUtils.waitForElementVisible(driver, createButton, Config.getInstance().getTimeOut());
    }

    public void clickOnCreateButton() {
        logger.INFO("Click on Create button");
        commonActions.click(createButton);
    }

    public boolean isEventPopupDisplayed() {
        logger.INFO("Check creating Event pop up displays");
        return commonActions.isDisplay(eventTitle);
    }

    public void inputEventInformation(String title, String startDate, String startTime, String endTime, String endDate,
                                      String guests, String location, String description) {
        logger.INFO("Enter event title");
        commonActions.sendKeys(this.eventTitle, title);
        logger.INFO("Select event option");
        commonActions.clickByJavaScript(this.eventOption);
        logger.INFO("Enter start date");
        commonActions.sendKeysAndTAB(this.startDate, startDate);
        logger.INFO("Enter start time");
        commonActions.sendKeysAndTAB(this.startTime, startTime);
        logger.INFO("Enter end time");
        commonActions.sendKeysAndTAB(this.endTime, endTime);
        logger.INFO("Enter start date");
        commonActions.sendKeysAndTAB(this.endDate, endDate);
        logger.INFO("Enter guest");
        commonActions.sendKeys(this.guest, guests);
        this.guest.sendKeys(Keys.ENTER);
        logger.INFO("Click on add location button");
        commonActions.clickByJavaScript(this.locationButton);
        logger.INFO("Click on add location");
        commonActions.clickByJavaScript(this.addLocation);
        logger.INFO("Enter location");
        commonActions.sendKeysAndEnter(this.location, location);
        logger.INFO("Click on add description");
        commonActions.clickByJavaScript(this.addDescription);
        logger.INFO("Enter description");
        commonActions.sendKeys(this.descriptions, description);
    }

    public void clickOnSaveButton() {
        logger.INFO("Click on Save button");
        commonActions.click(this.saveButton);
        if (commonActions.isDisplay(this.popUpConfirmation)) {
            commonActions.click(this.dontSendButton);
        }
    }

    public boolean isEventIsCreated(String eventTitle) {
        String xpath = "//span[text() = '" + eventTitle + "']";
        logger.INFO("Check event is created");
        return commonActions.isExistingElement(xpath, LocatorType.XPATH);
    }

    public String getMonthYearEvent() {
        return commonActions.getText(this.monthYear);
    }

    public int getDateEvent() {
        return Integer.parseInt(commonActions.getText(this.date));
    }

    public String getTimeEvent() {
        return commonActions.getText(this.time);
    }

    public void clickOnMoreOption() {
        logger.INFO("Click on More option button");
        commonActions.click(this.moreOption);
        WaitUtils.waitForElementClickable(driver, this.saveButton, Config.getInstance().getTimeOut());
    }

    public void inputOptionEventInformation(String title, String startDate, String startTime, String endTime, String endDate,
                                            String guests, String location, String description) {
        logger.INFO("Enter option event title");
        commonActions.sendKeys(this.title, title);
        logger.INFO("Enter option start date");
        commonActions.sendKeysAndTAB(this.startDate, startDate);
        logger.INFO("Enter option start time");
        commonActions.sendKeysAndTAB(this.startTime, startTime);
        logger.INFO("Enter option end time");
        commonActions.sendKeysAndTAB(this.endTime, endTime);
        logger.INFO("Enter option start date");
        commonActions.sendKeysAndTAB(this.endDate, endDate);
        logger.INFO("Enter guest");
        commonActions.sendKeys(this.guest, guests);
        this.guest.sendKeys(Keys.ENTER);
        logger.INFO("Enter location");
        commonActions.sendKeysAndEnter(this.location, location);
        logger.INFO("Enter description");
        commonActions.sendKeys(this.descriptions, description);
    }

}
