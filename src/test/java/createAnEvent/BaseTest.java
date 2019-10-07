package createAnEvent;

import com.aventstack.extentreports.ExtentReports;
import configure.Config;
import configure.ExtentManagerV4;
import configure.ExtentReportListener;
import configure.Log;
import helper.CommonActions;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import webdriver.WebDriverManager;

import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;

@Listeners(ExtentReportListener.class)
public class BaseTest {
  public static Config config = Config.getInstance();
  public WebDriverManager driverManager = WebDriverManager.getInstance();
  public WebDriver driver = null;
  public CommonActions commonActions;
  public String testcaseName;
  public SoftAssertions softAssertions = new SoftAssertions();
  protected SoftAssert sa;
//  public DataGenerate dataGenerate = new DataGenerate();
  protected Log logger = Log.getInstance();
//  protected ExcelBuilder excelBuilder = ExcelBuilder.getInstance();
  private ExtentReports extentManagerV4 = ExtentManagerV4.getExtent();

  @BeforeSuite(alwaysRun = true)
  public void beforeSuite(ITestContext iTestContext) {
    try {
      logger.INFO("Test suite name: " + iTestContext.getName());
      ExtentManagerV4.extentReportSetup();
    } catch (Exception e) {
      logger.ERROR(e.toString());
    }
  }

  @AfterSuite(alwaysRun = true)
  public void afterSuite(ITestContext iTestContext) {
    logger.INFO("End test suite name: " + iTestContext.getName());
    extentManagerV4.flush();
  }

  @BeforeClass(alwaysRun = true)
  public void beforeClass() {
    logger.INFO("Start test class Name: " + getClass().getName());
  }

  @AfterClass(alwaysRun = true)
  public void afterClass() {
    logger.INFO("End test class Name: " + getClass().getName());
  }

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod(Method method) {
    Test testCase = method.getAnnotation(Test.class);
    testcaseName = testCase.testName();
    ExtentManagerV4.test =
            extentManagerV4.createTest("TC_" + String.format("%03d", ExtentManagerV4.testCaseID) + "_" + testcaseName);

    logger.INFO("Start Test Case: " + testcaseName);
    try {
      driver = driverManager.setBrowserDriver();
      commonActions = new CommonActions(driver);
    } catch (Exception e) {
      logger.ERROR("Error while beforeMethod: " + e.toString());
    }
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod(Method method, ITestResult result) {
    logger.INFO("End Test Case: " + testcaseName);
    ExtentManagerV4.test.createNode(logger.readLogFile());

    // Close log file after each test case.
    driverManager.closeBrowser(driver);
    ExtentManagerV4.testCaseID++;
    Log.uninitialized();
  }

  public String getLogPath() {
    try {
      InetAddress ip = InetAddress.getLocalHost();
      return "http://"
              + ip.toString().trim()
              + File.separator
              + "Report"
              + File.separator
              + Log.logName
              + ".log";

    } catch (Exception e) {
      return "";
    }
  }

  public void createStep(String stepName) {
    ExtentManagerV4.test.createNode(stepName);
    logger.INFO(stepName);
  }

}
