package helper;

import configure.Log;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

public class WaitUtils {
  protected static Log logger = Log.getInstance();
  private static WebDriver jsWaitDriver;
  private static WebDriverWait jsWait;
  private static JavascriptExecutor jsExec;

  public static void waitForElementLocatedVisibility(
          WebDriver webDriver, By locator, int timeInSecond) {
    new WebDriverWait(webDriver, timeInSecond)
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  public static void waitForAjaxLoading(WebDriver driver, int timeInSecond) {
    new WebDriverWait(driver, timeInSecond)
            .until(
                    new ExpectedCondition<Boolean>() {
                        @Override
                        public Boolean apply(WebDriver driver) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return (Boolean) js.executeScript("return jQuery.active == 0");
                        }
                    });
  }

  public static void waitForLoadingJavaScript(WebDriver webDriver) {
    ExpectedCondition<Boolean> pageLoadCondition =
            new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    return ((JavascriptExecutor) driver)
                            .executeScript("return document.readyState")
                            .equals("complete");
                }
            };
    try {
      WebDriverWait wait = new WebDriverWait(webDriver, 20);
      wait.until(pageLoadCondition);
    } catch (Throwable error) {
      logger.ERROR("Timeout waiting for Page Load Request to complete.");
    }
  }

  public static void waitForLoadingElement(WebDriver driver, int timeInSecond, By loadingElement) {
    long starTime = System.currentTimeMillis();
    int retry1 = 0;
    logger.INFO("Waiting for loading element: " + loadingElement);
    while (true) {
      {
        try {
          waitForPresenceOfElementLocated(driver, loadingElement, 1);
          logger.INFO("Find loading icon at times: " + retry1);
          break;
        } catch (Exception e) {
          if (retry1 < 4) {
            WaitUtils.sleep(0.25);
            retry1++;
            continue;
          } else {
            break;
          }
        }
      }
    }
    int retry2 = 0;

    while (true) {
      if (retry1 == 5) {
        logger.INFO("There is NO loading icon.");
        break;
      }
      try {
        driver.findElement(loadingElement);
        if (retry2 < 15) {
          WaitUtils.sleep(0.25);
          retry2++;
        } else {
          break;
        }
      } catch (Exception e) {
        logger.INFO("Loading completely at retry time: " + retry2);
        break;
      }
    }
    logger.INFO("Wait for loading in miliseconds: " + (System.currentTimeMillis() - starTime));
    WaitUtils.sleep(0.25);
  }

  public static void waitForTryToFindElements(WebDriver driver, By locator, int totalTime) {
    logger.INFO("WaitForTryToFindElements: " + locator.toString());
    int retries = 1;
    while (true) {
      {
        try {
          driver.findElements(locator);
          logger.INFO("Find element at time " + retries);
          break;
        } catch (StaleElementReferenceException | NoSuchElementException e) {
          try {
            Thread.sleep(500);
          } catch (Exception e1) {
            continue;
          }
          if (retries * 0.5 < totalTime) {
            retries++;
            continue;
          } else {
            break;
          }
        }
      }
    }
  }

  public static void waitForTryToFindInElement(WebElement webElement, By locator, int totalTime) {
    logger.INFO("WaitForTryToFindElement: " + locator.toString());
    int retries = 1;
    while (true) {
      {
        try {
          webElement.findElement(locator);
          logger.INFO("Find element at time " + retries);
          break;
        } catch (StaleElementReferenceException | NoSuchElementException e) {
          try {
            Thread.sleep(500);
          } catch (Exception e1) {
            continue;
          }
          if (retries * 0.2 < totalTime) {
            retries++;
            continue;
          } else {
            break;
          }
        }
      }
    }
  }

  public static void waitForElementClickable(
          WebDriver driver, WebElement webElement, int timeInSecond) {
    logger.INFO("WaitForElementClickable: " + webElement.toString());
    try {
      new WebDriverWait(driver, timeInSecond)
              .until(ExpectedConditions.elementToBeClickable(webElement));
    } catch (Exception e) {
      logger.failedWithMessage(e.toString());
    }
  }

  public static void waitForElementVisible(
          WebDriver driver, WebElement webElement, int timeInSecond) {
    logger.INFO("WaitForElementVisible :" + webElement.toString());
    try {
      new WebDriverWait(driver, timeInSecond).until(ExpectedConditions.visibilityOf(webElement));
    } catch (Exception e) {
      logger.failedWithMessage(e.toString());
    }
  }

  public static void waitForElementInvisible(
          WebDriver driver, WebElement webElement, int timeInSecond) {
    logger.INFO("WaitForElementInvisible :" + webElement);
    new WebDriverWait(driver, timeInSecond).until(ExpectedConditions.invisibilityOf(webElement));
  }

  public static void waitForAttributeContains(
          WebDriver driver, By byLocator, String attribute, String value) {
    logger.INFO(
            String.format(
                    "Checking WebElement %s with given locator %s has attribute %s which contains value: %s",
                    byLocator, attribute, value));
  }

  public static void waitForAttributeContains(
          WebDriver driver, WebElement webElement, String attribute, String value) {
    logger.INFO(
            String.format(
                    "Checking WebElement %s with given locator %s has attribute %s which contains value: %s",
                    webElement, attribute, value));
  }

  public static void waitForPresenceOfElementLocated(
          WebDriver driver, By byLocator, int timeInSecond) {
    logger.INFO("WaitForPresenceOfElementLocated: " + byLocator.toString());
    new WebDriverWait(driver, timeInSecond)
            .until(ExpectedConditions.presenceOfElementLocated(byLocator));
  }

  public static void waitForElementClickableBy(WebDriver driver, By by, int timeInSecond) {
    new WebDriverWait(driver, timeInSecond).until(ExpectedConditions.elementToBeClickable(by));
  }

  public static void waitForElementInvisibleBy(WebDriver driver, int timeInSecond, By by) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSecond);
    wait.until(invisibilityOfElementLocated(by));
  }

  public static void waiForElementToBeSelected(
          WebDriver driver, WebElement webElement, int timeInSecond) {
    new WebDriverWait(driver, timeInSecond)
            .until(ExpectedConditions.elementToBeSelected(webElement));
  }

  public static void wairForStalenessOf(WebDriver driver, WebElement webElement, int timeOut) {
    logger.INFO("Wait until an element is no longer attached to the DOM: " + webElement);
    new WebDriverWait(driver, timeOut).until(ExpectedConditions.stalenessOf(webElement));
  }

  /**
   * How to Wait Until JQuery Angular and JS is ready
   *
   * @param driver {@link WebDriver}
   */
  public static void waitJQueryAngular(WebDriver driver, int timeOutInSecond) {
    logger.INFO("Wait for JQuery and Angular ready.");
    waitUntilJQueryReady(driver, timeOutInSecond);
    waitUntilAngularReady(driver, timeOutInSecond);
  }

  /**
   * How to Wait for JQuery Load
   *
   * @param driver {@link WebDriver}
   */
  public static void waitForJQueryLoad(WebDriver driver) {
    logger.INFO("Wait for JQuery to load.");
    // Wait for jQuery to load
    ExpectedCondition<Boolean> jQueryLoad =
            driver1 ->
                    ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);

    // Get JQuery is Ready
    logger.INFO("Get JQuery is Ready");
    boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active==0");

    // Wait JQuery until it is Ready!
    if (!jqueryReady) {
      logger.INFO("JQuery is NOT Ready!");
      System.out.println("JQuery is NOT Ready!");
      // Wait for jQuery to load
      jsWait.until(jQueryLoad);
    } else {
      logger.INFO("JQuery is Ready!");
      System.out.println("JQuery is Ready!");
    }
  }

  /**
   * How to Wait for Angular Load
   *
   * @param driver {@link WebDriver}
   */
  public static void waitForAngularLoad(WebDriver driver, int timeOut) {
    WebDriverWait wait = new WebDriverWait(driver, timeOut);
    JavascriptExecutor jsExec = (JavascriptExecutor) driver;

    String angularReadyScript =
            "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

    // Wait for ANGULAR to load
    logger.INFO("Get JQuery is Ready");
    ExpectedCondition<Boolean> angularLoad =
            driver1 ->
                    Boolean.valueOf(
                            ((JavascriptExecutor) driver1).executeScript(angularReadyScript).toString());

    // Get Angular is Ready
    boolean angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString());

    // Wait ANGULAR until it is Ready!
    logger.INFO("Wait ANGULAR until it is Ready!");
    if (!angularReady) {
      logger.INFO("ANGULAR is NOT Ready!");
      System.out.println("ANGULAR is NOT Ready!");
      // Wait for Angular to load
      wait.until(angularLoad);
    } else {
      logger.INFO("ANGULAR is Ready!");
      System.out.println("ANGULAR is Ready!");
    }
  }

  /**
   * How to Wait Until JQuery and JS Ready
   *
   * @param driver {@link WebDriver}
   */
  public static void waitUntilJQueryReady(WebDriver driver, int timeOut) {
    JavascriptExecutor jsExec = (JavascriptExecutor) driver;

    // First check that JQuery is defined on the page. If it is, then wait AJAX
    Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
    if (jQueryDefined == true) {
      // Pre Wait for stability (Optional)
      sleep(timeOut);

      // Wait JQuery Load
      waitForJQueryLoad(driver);

      // Wait JS Load
      waitUntilJSReady(driver, timeOut);

      // Post Wait for stability (Optional)
      sleep(timeOut);
    } else {
      System.out.println("jQuery is not defined on this site!");
    }
  }

  /**
   * How to Wait Until Angular and JS Ready
   *
   * @param driver {@link WebDriver}
   */
  public static void waitUntilAngularReady(WebDriver driver, int timeOut) {
    JavascriptExecutor jsExec = (JavascriptExecutor) driver;

    // First check that ANGULAR is defined on the page. If it is, then wait ANGULAR
    logger.INFO("Check whether ANGULAR is defined on the page or NOT.");
    Boolean angularUnDefined =
            (Boolean) jsExec.executeScript("return window.angular === undefined");
    if (!angularUnDefined) {
      logger.INFO("Angular is defined on the page");
      Boolean angularInjectorUnDefined =
              (Boolean)
                      jsExec.executeScript("return angular.element(document).injector() === undefined");
      if (!angularInjectorUnDefined) {
        // Pre Wait for stability (Optional)
        sleep(timeOut);

        // Wait Angular Load
        waitForAngularLoad(driver, timeOut);

        // Wait JS Load
        waitUntilJSReady(driver, timeOut);

        // Post Wait for stability (Optional)
        sleep(timeOut);
      } else {
        logger.INFO("Angular injector is not defined on this site!.");
        System.out.println("Angular injector is not defined on this site!");
      }
    } else {
      logger.WARN("Angular is not defined on this site!");
      System.out.println("Angular is not defined on this site!");
    }
  }

  /**
   * How to Wait Until JS Ready
   *
   * @param driver {@link WebDriver}
   */
  public static void waitUntilJSReady(WebDriver driver, int timeOut) {
    logger.INFO("Wait Javascript until it is Ready!");
    WebDriverWait wait = new WebDriverWait(driver, timeOut);
    JavascriptExecutor jsExec = (JavascriptExecutor) driver;

    // Wait for Javascript to load
    ExpectedCondition<Boolean> jsLoad =
            driver1 ->
                    ((JavascriptExecutor) driver)
                            .executeScript("return document.readyState")
                            .toString()
                            .equals("complete");

    // Get JS is Ready
    boolean jsReady =
            jsExec.executeScript("return document.readyState").toString().equals("complete");

    // Wait Javascript until it is Ready!
    logger.INFO("Check whether Javascript until it is Ready or NOT.");
    if (!jsReady) {
      logger.WARN("JS in NOT Ready!");
      System.out.println("JS in NOT Ready!");
      // Wait for Javascript to load
      wait.until(jsLoad);
    } else {
      logger.WARN("JS is Ready!");
      System.out.println("JS is Ready!");
    }
  }

  public static void waitForAlert(WebDriver driver, int timeOut) {
    logger.INFO("Wait for Alert.");
    new WebDriverWait(driver, timeOut).until(ExpectedConditions.alertIsPresent());
  }

  public static void wairForUrlContains(WebDriver driver, int timeOut, String text) {
    logger.INFO("Wait for Alert.");
    new WebDriverWait(driver, timeOut).until(ExpectedConditions.urlContains(text));
  }

  public static void waitForIsTitle(WebDriver driver, String title, int timeOutInSecond) {
    new WebDriverWait(driver, timeOutInSecond).until(ExpectedConditions.titleIs(title));
  }

  public static void waitForTextToBePresentInElement(
          WebDriver driver, WebElement webElement, String name, int timeOutInSecond) {
    logger.INFO("Wait for the visible text in element: " + name);
    new WebDriverWait(driver, timeOutInSecond)
            .until(ExpectedConditions.textToBePresentInElement(webElement, name));
  }

  public static void waitForTextToBePresentInLocator(
          WebDriver driver, By locator, String name, int timeOutInSecond) {
    logger.INFO("Wait for the text in locator: " + name);
    new WebDriverWait(driver, timeOutInSecond)
            .until(ExpectedConditions.textToBePresentInElementLocated(locator, name));
  }

  public static void waitForTitleMatch(WebDriver driver, String title, int timeOutInSecond) {
    new WebDriverWait(driver, timeOutInSecond).until(ExpectedConditions.urlMatches(title));
  }

  public static void s(WebDriver driver, By locator, int timeOutInSecond) {
    new WebDriverWait(driver, timeOutInSecond)
            .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
  }

  public static void s(WebDriver driver, String frameName, int timeOutInSecond) {
    new WebDriverWait(driver, timeOutInSecond)
            .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
  }

  public static void sleep(double seconds) {
    logger.INFO("Sleep in seconds: " + seconds);
    try {
      Thread.sleep((long) (seconds * 1000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public WebElement waitForElementToBeRefreshedAndClickable(WebDriver driver, By by, int timeOut) {
    return new WebDriverWait(driver, timeOut)
            .until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(by)));
  }
}
