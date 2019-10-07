package configure;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import webdriver.WebDriverManager;

import java.text.SimpleDateFormat;
import java.util.Date;


public class
ExtentReportListener implements ITestListener {
  public WebDriverManager driverManager = WebDriverManager.getInstance();
  protected Log logger = Log.getInstance();

  @Override
  public void onTestStart(ITestResult iTestResult) {}

  @Override
  public void onTestSuccess(ITestResult iTestResult) {
    ExtentManagerV4.test.log(
        Status.PASS,
        MarkupHelper.createLabel(iTestResult.getName() + "_Result: PASSED.", ExtentColor.GREEN));
    logger.INFO("Result: PASSED");
  }

  @Override
  public void onTestFailure(ITestResult iTestResult) {
    // MarkupHelper is used to display the output in different colors
    ExtentManagerV4.test.log(
        Status.FAIL,
        MarkupHelper.createLabel(iTestResult.getName() + "_Result: FAILED.", ExtentColor.RED));
    ExtentManagerV4.test.log(Status.FAIL, iTestResult.getThrowable());
    String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    driverManager.captureFullPageScreenShot(fileName);
    try {
      ExtentManagerV4.test.addScreenCaptureFromPath(
          PathConstant.screenshotPath + fileName + ".jpg");
    } catch (Exception e) {
      logger.WARN("Failed to capture screenshot: " + e.toString());
    }
    logger.INFO("Result: FAILED");
  }

  @Override
  public void onTestSkipped(ITestResult iTestResult) {
    ExtentManagerV4.test.log(
        Status.SKIP,
        MarkupHelper.createLabel(iTestResult.getName() + "_Result: SKIP.", ExtentColor.ORANGE));
    logger.INFO("Result: SKIP");
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {}

  @Override
  public void onStart(ITestContext iTestContext) {
    logger.INFO("Start Testing");
  }

  @Override
  public void onFinish(ITestContext iTestContext) {
    logger.INFO("Done Testing");
  }
}
