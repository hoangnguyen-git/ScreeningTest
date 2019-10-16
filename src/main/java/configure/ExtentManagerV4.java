package configure;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManagerV4 {
  public static int testCaseID = 0;
  public static ExtentTest test;
  private static ExtentReports extentReport;
  private static ExtentHtmlReporter htmlReporter;

  public static synchronized ExtentReports getExtent() {
    if (extentReport == null) {
      extentReport = new ExtentReports();
      testCaseID++;
    }
    return extentReport;
  }

  public static void extentReportSetup() {
    htmlReporter = new ExtentHtmlReporter(PathConstant.reportPath + "AutomationReport.html");
    extentReport.attachReporter(htmlReporter);

    htmlReporter.config().setDocumentTitle("Hoang Nguyen Automation Report");
    htmlReporter.config().setReportName("Hoang Nguyen Report");
    htmlReporter.config().setTheme(Theme.DARK);
    htmlReporter.config().setEncoding("UTF-8");

    // General information related to application
    extentReport.setSystemInfo("Application Name", "Hoang Nguyen Report");
    extentReport.setSystemInfo("User Name", "Hoang Company Development Team");
    extentReport.setSystemInfo("Environment", "SIT");
  }
}
