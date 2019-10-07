package helper;

import configure.Config;
import configure.Log;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.*;

public class CommonActions {
  public WebDriver webDriver;
  protected Config config = Config.getInstance();
  private Log logger = Log.getInstance();

  public CommonActions(WebDriver driver) {
    webDriver = driver;
  }

  /**
   * ************************************
   * =========VERIFY UI=================
   */
  public boolean isDisplay(WebElement webElement) {
    WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
    if (webElement.isDisplayed()) {
      return true;
    } else {
      logger.INFO("isDisplay" + webElement.isDisplayed());
      return false;
    }
  }

  public boolean isEnabled(WebElement webElement) {
    WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
    if (webElement.isEnabled()) {
      return true;
    } else {
      logger.INFO("isEnable" + webElement.isEnabled());
      return false;
    }
  }

  public boolean isEnabledDisplay(WebElement webElement) {
    WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
    if (webElement.isEnabled() && webElement.isDisplayed()) {
      return true;
    } else {
      logger.INFO(
          "isEnable " + webElement.isEnabled() + " isDisplayed: " + webElement.isDisplayed());
      return false;
    }
  }

  public boolean isSelected(WebElement webElement) {
    WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
    if (webElement.isSelected()) {
      return true;
    } else {
      logger.INFO("Disabled");
      return false;
    }
  }

  /**
   * ************************************
   *========FIND ELEMENTS================
   *
   * <p>/** Find element in other element
   *
   * @param webElement {@link WebDriver}
   * @param element
   * @param locatorType
   */
  public WebElement findElementInElement(
          WebElement webElement, String element, LocatorType locatorType) {
    logger.INFO("Find element in other element: " + webElement);
    WebElement webElement1;
    By elementLocated = findByLocator(element, locatorType);
    WaitUtils.waitForElementLocatedVisibility(webDriver, elementLocated, config.getTimeOut());
    try {
      webElement1 = webElement.findElement(findByLocator(element, locatorType));
    } catch (Exception e) {
      logger.failedWithMessage("Failed to find element:\n" + e.toString());
      return null;
    }
    logger.INFO("Found element: " + webElement1);
    return webElement1;
  }

  /**
   * Find elements in other element
   *
   * @param webElement {@link WebDriver}
   * @param element
   * @param locatorType
   */
  public List<WebElement> findElementsInElement(
          WebElement webElement, String element, LocatorType locatorType) {
    List<WebElement> webElements;
    try {
      webElements = webElement.findElements(findByLocator(element, locatorType));
    } catch (Exception e) {
      logger.failedWithMessage("Failed to find elements :\n" + e.toString());
      return null;
    }
    return webElements;
  }

  public boolean isExistingElement(String element, LocatorType locatorType) {
    long startTime = System.currentTimeMillis();
    int trytime = 1;
    boolean result = false;
    while (true) {
      try {
        webDriver.findElement(findByLocator(element, locatorType));
        logger.INFO("Found element :" + locatorType.toString() + " " + element);
        result = true;
        break;
      } catch (Exception e) {
        WaitUtils.sleep(0.7);
        if (trytime < 3) {
          trytime++;
          continue;
        } else {
          result = false;
          break;
        }
      }
    }
    logger.INFO(
        "Check the visible element in miliseconds: " + (System.currentTimeMillis() - startTime));
    return result;
  }

  public boolean isExistingElements(String element, LocatorType locatorType) {
    int trytime = 1;
    boolean result = false;
    while (true) {
      try {
        webDriver.findElements(findByLocator(element, locatorType));
        logger.INFO("Found element :" + locatorType.toString() + " " + element);
        result = true;
        break;
      } catch (Exception e) {
        WaitUtils.sleep((long) 0.5);
        if (trytime < 3) {
          trytime++;
          continue;
        } else {
          result = false;
          break;
        }
      }
    }
    return result;
  }

  /**
   * Find element
   *
   * @param element
   * @param locatorType
   */
  public WebElement findElement(String element, LocatorType locatorType) {
    return webDriver.findElement(findByLocator(element, locatorType));
  }

  /**
   * Find element
   *
   * @param element
   * @param locatorType
   */
  public List<WebElement> findElements(String element, LocatorType locatorType) {
    return webDriver.findElements(findByLocator(element, locatorType));
  }

  /**
   * Find By locator
   *
   * @param element
   * @param locatorType
   */
  public By findByLocator(String element, LocatorType locatorType) {
    By byLocator = null;
    try {
      switch (locatorType) {
        case ID:
          byLocator = By.id(element);
          break;
        case NAME:
          byLocator = By.name(element);
          break;
        case CSS:
          byLocator = By.cssSelector(element);
          break;
        case XPATH:
          byLocator = By.xpath(element);
          break;
        case TAGNAME:
          byLocator = By.tagName(element);
          break;
        case LINKTEXT:
          byLocator = By.linkText(element);
          break;
        case CLASSNAME:
          byLocator = By.className(element);
          break;
        case PARTIALLINKTEXT:
          byLocator = By.partialLinkText(element);
          break;
        default:
      }
    } catch (Exception e) {
      logger.ERROR("Failed to find By locator:\n" + e.toString());
      return null;
    }
    return byLocator;
  }

  /**
   * ***************************************************************
   * ===============================================================
   * ==================== TEXT BOX =================================
   * ===============================================================
   */

  /**
   * Send keys to text box .
   *
   * @param webElement {@link WebDriver}
   */
  public void sendKeys(WebElement webElement, String text) {
    logger.INFO("SendKeys: " + text + " in element: " + webElement);
    try {
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      Actions actions = new Actions(webDriver);
      actions.moveToElement(webElement).perform();
      webElement.clear();
      webElement.sendKeys(text);
      actions.release(webElement);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to send text. \n" + e.toString());
    }
  }

  /**
   * Send keys slowly to text box .
   *
   * @param webElement {@link WebDriver}
   */
  public void sendKeysSlowly(WebElement webElement, String text) {
    logger.INFO("SendKeys: " + text + " in element: " + webElement);
    try {
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      Actions actions = new Actions(webDriver);
      actions.moveToElement(webElement).perform();
      webElement.click();
      webElement.clear();
      for (int i = 0; i < text.length(); i++) {
        webElement.sendKeys(Character.toString(text.charAt(i)));
        WaitUtils.sleep(1);
      }
      webElement.sendKeys(Keys.TAB);
      actions.release(webElement);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to send text. \n" + e.toString());
    }
  }

  public void sendUploadfile(WebElement webElement, String fileName, double waitTime) {
    logger.INFO("Upload file: " + fileName);
    try {
      scrollToElementByJS(webElement);
      String pathFile =
          System.getProperty("user.dir")
              + File.separator
              + "UploadFile"
              + File.separator
              + fileName;
      webElement.sendKeys(pathFile);
    } catch (Exception e) {
      logger.failedWithMessage(e.toString());
    }
    WaitUtils.sleep(waitTime);
  }

  /**
   * Send keys and TAB to text box .
   *
   * @param webElement {@link WebDriver}
   */
  public void sendKeysAndTAB(WebElement webElement, String text) {
    logger.INFO("SendKeys: " + text + " in element: " + webElement);
    try {
      Actions action = new Actions(webDriver);
      action.moveToElement(webElement).perform();
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      webElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
      webElement.sendKeys(Keys.DELETE);
      webElement.sendKeys(text);
      webElement.sendKeys(Keys.TAB);
      action.release(webElement);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to send text. \n" + e.toString());
    }
  }

  /**
   * Send keys and ENTER to text box .
   *
   * @param webElement {@link WebDriver}
   */
  public void sendKeysAndEnter(WebElement webElement, String text) {
    logger.INFO("SendKeys: " + text + " in element: " + webElement);
    try {
      Actions action = new Actions(webDriver);
      action.moveToElement(webElement).perform();
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      webElement.clear();
      webElement.sendKeys(text);
      webElement.sendKeys(Keys.ENTER);
      action.release(webElement);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to send text. \n" + e.toString());
    }
  }

  public void clearTextByKeys(WebElement webElement) {
    logger.INFO("Clear text by Keys: CTR + A DELETE");
    try {
      Actions action = new Actions(webDriver);
      action.moveToElement(webElement).perform();
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      webElement.sendKeys(Keys.CONTROL, "a");
      webElement.sendKeys(Keys.DELETE);
      action.release(webElement);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to clear text by Keys. \n" + e.toString());
    }
  }

  /**
   * Clear text in text box .
   *
   * @param webElement {@link WebDriver}
   */
  public void clear(WebElement webElement) {
    logger.INFO("Clear text on element: " + webElement);
    try {
      ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", webElement);
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      if (webElement.isEnabled()) {
        webElement.clear();
      } else {
        logger.ERROR("Element is disabled.");
      }
    } catch (Exception e) {
      logger.failedWithMessage("Failed to clear text box. \n" + e.toString());
    }
  }

  /**
   * Select option from DDL by index.
   *
   * @param webElement {@link WebDriver}
   * @param index Data of element from Excel file.
   */
  public void selectByIndex(WebElement webElement, int index) {
    Select select = new Select(webElement);
    try {
      logger.INFO("Select element with index: " + index);
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      select.selectByIndex(index);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to select by index from DDL.\n " + e.toString());
    }
  }

  /**
   * *********************************************************
   * =========================================================
   * ================ DROP DOWN LIST==========================
   * =========================================================
   */

  /**
   * Select option from DDL by index.
   *
   * @param webElement {@link WebDriver}
   * @param valueOption the value o
   */
  public void selectByValue(WebElement webElement, String valueOption) {
    Actions mouse = new Actions(webDriver);
    mouse.moveToElement(webElement).perform();
    Select select = new Select(webElement);
    try {
      logger.INFO("Select element with value: " + valueOption);
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      select.selectByValue(valueOption);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to select option by value from DDL.\n" + e.toString());
    }
  }

  /**
   * Select option from DDL by index.
   *
   * @param webElement {@link WebDriver}
   * @param valueOption the value o
   */
  public void selectByText(WebElement webElement, String valueOption) {
    Select select = new Select(webElement);
    try {
      logger.INFO("Select element by text: " + valueOption);
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      select.selectByVisibleText(valueOption);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to select option by text from DDL.\n" + e.toString());
    }
  }

  /**
   * Check option whether it is in drop down list or NOT.
   *
   * @param webElement element
   * @param optionValue value in option
   * @return true or false
   */
  public boolean isTextPresentInTheDropdown(WebElement webElement, String optionValue) {
    Select select = new Select(webElement);
    try {
      WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
      return select
          .getOptions()
          .parallelStream()
          .anyMatch(option -> option.getText().contains(optionValue));
    } catch (Exception e) {
      logger.failedWithMessage(e.toString());
      return false;
    }
  }

  /**
   * Get the list of options in drop down list.
   *
   * @param webElement element
   * @return number of options
   */
  public List<String> getListOfOptions(WebElement webElement) {
    logger.INFO("Get all options of DDL");
    List<String> options = new ArrayList<>();
    try {
      Select select = new Select(webElement);
      WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
      List<WebElement> optionList = select.getOptions();
      for (WebElement option : optionList) {
        options.add(option.getText().trim());
      }
      logger.INFO("List of option DDL: " + options.toString());
      return options;
    } catch (Exception e) {
      logger.failedWithMessage(e.toString());
      return null;
    }
  }

  /**
   * Get the default option of DDL.
   *
   * @param webElement element
   * @return option name
   */
  public String getCurrentSelectdOption(WebElement webElement) {
    try {
      logger.INFO("Get the default selected value");
      Actions mouse = new Actions(webDriver);
      mouse.moveToElement(webElement).perform();
      WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
      Select select = new Select(webElement);
      if (select != null) {
        logger.INFO("Default selected value: " + select.getFirstSelectedOption().getText().trim());
        return select.getFirstSelectedOption().getText().trim();
      } else {
        logger.ERROR("Failed to get current option: Select null");
        return null;
      }

    } catch (Exception e) {
      logger.failedWithMessage("Failed to get current option: \n" + e.toString());
      return null;
    }
  }

  /**
   * To verify whether a dropdown allows multiple selections or not.. This method is a boolean
   * method which returns true if the dropdown allows multiple selections else will return false.
   *
   * @param webElement {@link WebElement}
   */
  public boolean isMultiple(WebElement webElement) {
    Select dropdown = new Select(webElement);
    logger.INFO("Check the DDL multiple");
    boolean status = dropdown.isMultiple();
    if (status) {
      logger.INFO("Allows multiple selection.");
    } else {
      logger.ERROR("Not allow multiple selection.");
    }
    return status;
  }

  /**
   * ***************************************************
   * ===================================================
   * ================ MOUSE ACTIONS ====================
   * ===================================================
   */
  public void hoverMouseOnWebElement(WebElement webElement) {
    try {
      Actions actions = new Actions(webDriver);
      actions.moveToElement(webElement).build().perform();
    } catch (Exception e) {
      logger.failedWithMessage(e.toString());
    }
  }
  /**
   * How to right clickWithActionClass by Mouse
   *
   * @param webElement {@link WebElement}
   */
  public void rightClick(WebElement webElement) {
    try {
      logger.INFO("Left Click by Mouse.");
      Actions actions = new Actions(webDriver);
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      actions.contextClick(webElement).perform();
    } catch (Exception e) {
      logger.failedWithMessage("Error when left clickWithActionClass:\n" + e.toString());
    }
  }

  /**
   * ***********************************************************
   * ===========================================================
   * ================== COMMON ACTIONS =========================
   * ===========================================================
   */
  public void clickWithActionClass(WebElement webElement) {
    try {
      logger.INFO("Click on element: " + webElement);
      ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", webElement);
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      Actions actions = new Actions(webDriver);
      if (webElement.isEnabled()) {
        actions.moveToElement(webElement).click().perform();
        actions.release();
      } else {
        logger.ERROR("Element is disabled.");
      }
    } catch (Exception e) {
      logger.failedWithMessage("Failed to clickWithActionClass on element. \n" + e.toString());
    }
  }

  public void click(WebElement webElement) {
    try {
      logger.INFO("Click on element: " + webElement);
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      Actions actions = new Actions(webDriver);
      actions.moveToElement(webElement).click().perform();
      actions.release();
    } catch (Exception e) {
      logger.failedWithMessage("Failed to click on element. \n" + e.toString());
    }
  }
  /**
   * To check all option of checkboxes
   *
   * @param checkBoxes {@link WebElement}
   */
  public void checkAllCheckbox(List<WebElement> checkBoxes) {
    logger.INFO("Check all checkboxes");
    try {
      if (checkBoxes != null & checkBoxes.size() > 0) {
        checkBoxes.forEach(
            checkbox ->
                WaitUtils.waitForElementClickable(webDriver, checkbox, config.getTimeOut()));
        checkBoxes.forEach(checkbox -> checkbox.click());
      }
    } catch (Exception e) {
      logger.failedWithMessage("Failed to check all checkboxes.");
    }
  }

  /**
   * To submit form
   *
   * @param webElement {@link WebElement}
   */
  public void submit(WebElement webElement) {
    try {
      logger.INFO("Submit the form");
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      if (webElement.isEnabled()) {
        webElement.submit();
      } else {
        logger.ERROR("Element is disabled.");
      }
    } catch (Exception e) {
      logger.failedWithMessage("Failed to submit element. \n" + e.toString());
    }
  }

  public void doubleClick(WebElement webElement) {
    try {
      logger.INFO("Double clickWithActionClass on element.");
      ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", webElement);
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      Actions actions = new Actions(webDriver);
      if (webElement.isEnabled()) {
        actions.moveToElement(webElement).doubleClick().perform();
        actions.release();
      }
    } catch (Exception e) {
      logger.failedWithMessage("Failed to double clickWithActionClass on element. \n" + e.toString());
    }
  }

  public void mouseOver(WebElement webElement) {
    try {
      logger.INFO("MouseOver the element.");
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
      Actions actions = new Actions(webDriver);
      if (webElement.isEnabled()) {
        actions.moveToElement(webElement);
        actions.click().perform();
        actions.release();
      } else {
        logger.ERROR("Element is disabled.");
      }
    } catch (Exception e) {
      logger.failedWithMessage("Failed to MouseOver.\n" + e.toString());
    }
  }

  public void drapAndDrop(WebElement source, WebElement destination) {
    try {
      logger.INFO("Drap and Drop element.");
      WaitUtils.waitForElementClickable(webDriver, source, config.getTimeOut());
      WaitUtils.waitForElementClickable(webDriver, destination, config.getTimeOut());
      Actions actions = new Actions(webDriver);
      if (source.isEnabled() && destination.isEnabled()) {
        actions.dragAndDrop(source, destination).build().perform();
        actions.release();
      }
    } catch (Exception e) {
      logger.failedWithMessage("Failed to DrapAndDrop.\n" + e.toString());
    }
  }

  public String getText(WebElement webElement) {
    try {
      logger.INFO("Get text the element: " + webElement);
      WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
      return webElement.getText().trim();
    } catch (Exception e) {
      logger.failedWithMessage("Failed to get text. \n" + e.toString());
      return "";
    }
  }

  public String getText(By locator) {
    WaitUtils.waitForPresenceOfElementLocated(webDriver, locator, config.getTimeOut());
    return webDriver.findElement(locator).getText().trim();
  }

  /**
   * How to get attribute value of element
   *
   * @param webElement
   * @param attributeName
   */
  public String getAttribute(WebElement webElement, String attributeName) {
    try {
      logger.INFO("Get the value of attribute: " + attributeName + " of element " + webElement);
      WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
      logger.INFO("Value: " + webElement.getAttribute(attributeName));
      return webElement.getAttribute(attributeName);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to get the value of attribute. \n" + e.toString());
      return "";
    }
  }

  /**
   * How to get value of value attribute element
   *
   * @param webElement
   */
  public String getValueAttribute(WebElement webElement) {
    try {
      logger.INFO("Get the value  attribute of element " + webElement);
      WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
      logger.INFO("Value: " + webElement.getAttribute("value"));
      return webElement.getAttribute("value");
    } catch (Exception e) {
      logger.failedWithMessage("Failed to get the value of attribute. \n" + e.toString());
      return "";
    }
  }

  public void setAttribute(WebElement webElement, String attributeName, String value) {
    try {
      logger.INFO("Set into value attribute with value: " + value);
      Actions actions = new Actions(webDriver);
      actions.moveToElement(webElement);
      WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
      JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
      jsExecutor.executeScript(
          "arguments[0].setAttribute(arguments[1], arguments[2]);",
          webElement,
          attributeName,
          value);
    } catch (Exception e) {
      logger.failedWithMessage(e.toString());
    }
  }

  public boolean isRequiredField(WebElement webElement) {
    try {
      logger.INFO("Check required field of element: " + webElement);
      WaitUtils.waitForElementVisible(webDriver, webElement, config.getTimeOut());
      return Boolean.parseBoolean(webElement.getAttribute("required"));
    } catch (Exception e) {
      logger.failedWithMessage(e.toString());
      return false;
    }
  }

  /**
   * How to set Browser size
   *
   * @param width the width of browser
   * @param high the high of browser
   */
  public void setBrowserSize(int width, int high) {
    webDriver.manage().window().setSize(new Dimension(width, high));
  }

  /** How to switch to current page */
  public void switchToCurrentPage() {
    String handle = webDriver.getWindowHandle();
    for (String tempHandle : webDriver.getWindowHandles()) {
      if (tempHandle.equals(handle)) {
        webDriver.close();
      } else {
        webDriver.switchTo().window(tempHandle);
      }
    }
  }

  /** How to verify HTML5 message. */
  public String getHtml5ValidationMessage(WebElement element) {
    JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
    return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", element);
  }

  /**
   * Gets the all frames on page.
   *
   * @param driver the driver
   * @return the all frames on page
   */
  public List<WebElement> getAllFramesOnPage(WebDriver driver) {

    List<WebElement> iFarmeList = new ArrayList<WebElement>();
    try {
      iFarmeList = driver.findElements(By.tagName("iframe"));
      return iFarmeList;
    } catch (Exception e) {
      logger.failedWithMessage(e.toString());
      return null;
    }
  }
  /**
   * ************************************************************
   * ============================================================
   * ========================= FRAME ============================
   * ============================================================
   */

  /**
   * Switch on frame by name.
   *
   * @param driver the driver
   * @param frameName the frame name
   */
  public void switchOnFrameByName(WebDriver driver, String frameName) {
    try {
      List<WebElement> iFarmeList = getAllFramesOnPage(driver);
      if (iFarmeList != null) {
        driver.switchTo().frame(frameName);
      }
    } catch (Exception e) {
      logger.failedWithMessage("Failed to switch to Frame: " + frameName + "\n" + e.toString());
    }
  }

  /**
   * Switch on frame by id.
   *
   * @param driver the driver
   * @param frameId the frame id
   */
  public void switchOnFrameById(WebDriver driver, String frameId) {
    try {
      List<WebElement> iFarmeList = getAllFramesOnPage(webDriver);
      if (iFarmeList != null) {
        driver.switchTo().frame(frameId);
      }
    } catch (Exception e) {
      logger.failedWithMessage("Failed to switch to frameID: " + frameId + "\n" + e.toString());
    }
  }

  /**
   * Switch to main page from iFrame.
   *
   * @param driver the driver
   */
  public void switchToMainPageFromIFrame(WebDriver driver) {
    try {
      driver.switchTo().defaultContent();
    } catch (Exception e) {
      logger.failedWithMessage("Failed to Main Page from iFrame.\n" + e.toString());
    }
  }

  /**
   * Gets the iFrame count.
   *
   * @param driver the driver
   * @return the i frame count
   */
  public int getIFrameCount(WebDriver driver) {
    if (getAllFramesOnPage(webDriver) != null) {
      return getAllFramesOnPage(driver).size();
    } else {
      return 0;
    }
  }

  /**
   * *************************************************************
   * ============================================================= ====================== ALERT
   * =============================== =============================================================
   */

  /** Accept alert. */
  public void acceptAlert() {
    try {
      logger.INFO("Accept alert.");
      WaitUtils.waitForAlert(webDriver, config.getTimeOut());
      Alert alert = webDriver.switchTo().alert();
      alert.accept();
    } catch (Exception e) {
      logger.failedWithMessage("Failed to accept alert");
    }
  }

  /**
   * Dismiss alert.
   *
   * @param driver the driver
   */
  public void dismissAlert(WebDriver driver) {
    try {
      logger.INFO("Dismiss alert.");
      WaitUtils.waitForAlert(webDriver, config.getTimeOut());
      Alert alert = driver.switchTo().alert();
      alert.dismiss();
    } catch (Exception e) {
      logger.failedWithMessage("Failed to dismiss alert");
    }
  }

  /** Send keys in Accept alert. */
  public void sendKeysInAlert(String keys) {
    try {
      logger.INFO("Send keys in alert.");
      WaitUtils.waitForAlert(webDriver, config.getTimeOut());
      Alert alert = webDriver.switchTo().alert();
      alert.sendKeys(keys);
      alert.accept();
    } catch (Exception e) {
      logger.failedWithMessage("Failed to accept alert");
    }
  }

  /**
   * ************************************************************
   * ============================================================ ======================== COOKIE
   * ============================ ============================================================
   */
  public CookieStore seleniumCookiesToCookieStore() {

    Set<Cookie> seleniumCookies = webDriver.manage().getCookies();
    CookieStore cookieStore = new BasicCookieStore();

    for (Cookie seleniumCookie : seleniumCookies) {
      BasicClientCookie basicClientCookie =
          new BasicClientCookie(seleniumCookie.getName(), seleniumCookie.getValue());
      basicClientCookie.setDomain(seleniumCookie.getDomain());
      basicClientCookie.setExpiryDate(seleniumCookie.getExpiry());
      basicClientCookie.setPath(seleniumCookie.getPath());
      cookieStore.addCookie(basicClientCookie);
    }
    return cookieStore;
  }

  /**
   * How to delete cookies
   *
   * @param cookieName cookie name
   */
  public void deleteCookie(String cookieName) {
    webDriver.manage().deleteCookieNamed(cookieName);
  }

  /** How to delete all cookies */
  public void deleteAllCookies() {
    webDriver.manage().deleteAllCookies();
  }

  /**
   * How to delete all cookies
   *
   * @return Cookies Name in Map
   */
  public Map<String, String> getCookieByName(String cookieName) {
    Cookie cookie = webDriver.manage().getCookieNamed(cookieName);
    if (cookie != null) {
      Map<String, String> map = new HashMap<>();
      map.put("name", cookie.getName());
      map.put("value", cookie.getValue());
      map.put("path", cookie.getPath());
      map.put("domain", cookie.getDomain());
      map.put("expiry", cookie.getExpiry().toString());
      return map;
    }
    return null;
  }

  /**
   * How to get all cookies
   *
   * @param driver {@link WebDriver}
   */
  public Set<Cookie> getAllCookies(WebDriver driver) {
    return driver.manage().getCookies();
  }

  /**
   * How to clickWithActionClass by JavaScript
   *
   * @param webElement {@link WebDriver}
   */
  public void clickByJavaScript(WebElement webElement) {
    try {
      logger.INFO("Click by JavaScript");
      WaitUtils.waitForElementClickable(webDriver, webElement, config.getTimeOut());
        if (webElement.isEnabled() && webElement.isDisplayed()) {
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", webElement);
        } else {
            System.out.println("Unable to click on element");
        }
    } catch (StaleElementReferenceException e) {
        System.out.println("Element is not attached to the page DOCUMENT " + e.getStackTrace());
    } catch (java.util.NoSuchElementException e) {
        System.out.println("Element was not found in DOM " + e.getStackTrace());
    } catch (Exception e) {
        System.out.println("Unable to click on element " + e.getStackTrace());
    }
  }

  /**
   * **********************************************************
   * ========================================================== ======================== SCROLL
   * ========================== ==========================================================
   */

  /**
   * How to scroll to element by JavaScript
   *
   * @param webElement {@link WebDriver}
   */
  public void scrollToElementByJS(WebElement webElement) {
    logger.INFO("Scroll to element by JavaScript");
    try {
      JavascriptExecutor jse = (JavascriptExecutor) webDriver;
      jse.executeScript("arguments[0].scrollIntoView();", webElement);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to scroll to element by JavaScript.\n" + e.toString());
    }
  }

  public void scrollToHeighByJS() {
    logger.INFO("Scroll down to height by JavaScript");
    try {
      ((JavascriptExecutor) webDriver)
          .executeScript("window.scrollTo(0,document.body.scrollHeight)");
    } catch (Exception e) {
      logger.failedWithMessage("Failed to scroll to element by JavaScript.\n" + e.toString());
    }
  }

  /** How to refresh browser by JavaScript */
  public void refreshBrowserByJS() {
    try {
      JavascriptExecutor js = (JavascriptExecutor) webDriver;
      js.executeScript("history.go(0);");
    } catch (Exception e) {
      logger.failedWithMessage(e.toString());
    }
  }

  /**
   * How to highlight element by JavaScript
   *
   * @param webElement {@link WebDriver}
   */
  public void highlightElement(WebElement webElement) {
    try {
      logger.INFO("Highlight element by JavaScript");
      JavascriptExecutor js = (JavascriptExecutor) webDriver;
      js.executeScript("arguments[0].style.border='3px dotted blue'", webElement);
    } catch (Exception e) {
      logger.failedWithMessage("Failed to highlight element by JavaScript.");
    }
  }

  /**
   * scrollTo WebElement
   *
   * @param element
   */
  public void scrollToElementCoordinate(WebElement element) {
    Coordinates coordinate = ((Locatable) element).getCoordinates();
    coordinate.onPage();
    coordinate.inViewPort();
  }

  /**
   * **********************************************************
   * ========================================================== ======================== TAB
   * ============================= ==========================================================
   */
}
