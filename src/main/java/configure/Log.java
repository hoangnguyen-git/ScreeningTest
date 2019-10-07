package configure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import helper.FileUtility;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

public class Log {
    public static String logName;
    /**
     * Logger of log4j
     */
    private static Logger logger = null;
    /**
     * Singleton variable for Log object
     */
    private static Log log;

    /**
     * Create log file with log name.
     */

    public static void saveLogName() {
        // Path of log4j configuration xml
        String logPath = "log4j.xml";
        logName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + "_" + System.currentTimeMillis();
        // Change name for log file.
        FileUtility.replaceInFile(logPath, "Summary_", ".", logName);
        logName = "Summary_" + Log.logName;
    }

    /**
     * Close log file
     */
    public static void uninitialized() {
        if (log != null) {
            log.INFO("================================END TEST CASE=======================================");
            log = null;

        }
    }

    /**
     * Config log4j with log4j.xml, the Log name define in param name tag with format:
     * Summary_yyyyMMdd_HHmmss.log
     */
    public static void startWriteSummaryLog() {
        DOMConfigurator.configure("log4j.xml");
    }

    /**
     * Create log folder in project folder
     */
    public static void createLogFolder() {
        String filePath = File.separator + "Log";
        FileUtility.createFolder(filePath);
    }

    /**
     * Create new log, if exist, return instance
     *
     * @return Log
     */
    public static Log getInstance() {
        if (log == null) {
            log = new Log();
            createLogFolder();
            saveLogName();
            startWriteSummaryLog();
            logger = Logger.getLogger(Log.class.getName());
//            FileUtility.createFolder(File.separator + "ScreenShot");
//            FileUtility.createFolder(File.separator + "Video");
//            FileUtility.createFolder(File.separator + "FileDownLoad");
//            FileUtility.createFolder(File.separator + "Report");
        }
        return log;
    }

    /**
     * Write log level info
     *
     * @param message message write to log
     */
    public void INFO(String message) {

        logger.info(message);
    }

    /**
     * Write log level warning
     *
     * @param message message write to log
     */
    public void WARN(String message) {

        logger.warn(message);
    }

    /**
     * Write log level error
     *
     * @param message message write to log
     */
    public void ERROR(String message) {

        logger.error(message);
    }

    /**
     * Write log level fatal
     *
     * @param message message write to log
     */
    public void FATAL(String message) {

        logger.fatal(message);
    }

    /**
     * Write log level debug
     *
     * @param message message write to log
     */
    public void DEBUG(String message) {

        logger.debug(message);
    }

    /**
     * Check condition, if false, write log message and make fail testcase
     *
     * @param message   message write to log when condition false
     * @param condition condition to check
     */
    public void logAssertion(boolean condition, String message) {
        if (!condition) {
            failedWithMessage(message);
        }
    }
    /**
     * Compare two strings
     * @param message   message write to log when condition false
     * @param expected  expected
     * @param actual    actual
     */
    public void logAssertion(String message, String expected, String actual) {
        try {
            Assert.assertEquals(expected.equals(actual), message);
        } catch (AssertionError e) {
            logger.error("Expected result: " + expected);
            failedWithMessage(message + ". Actual result: " + actual);
        }
    }
    /**
     * Compare two json string
     * @param message   message write to log when condition false
     * @param expected  expected of element
     * @param actual    actual of element
     */
    public void logAssertEqualsJson(String message, String expected, String actual) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonExpected = mapper.readTree(expected);
            JsonNode jsonActual = mapper.readTree(actual);
            Assert.assertTrue(jsonExpected.equals(jsonActual));
        } catch (AssertionError | IOException e) {
            logger.error("Expected result: " + expected);
            failedWithMessage(message + ". Actual result: " + actual);
        }
    }
    public void assertEquals(boolean condition, String message) {
        try {
            Assert.assertEquals(condition, message);
        } catch (AssertionError e) {
            logger.error(message);
            failedWithMessage(e.toString());
        }
    }

    /**
     * Compare two List string. If failed, failed and stop test case
     *
     * @param expected
     * @param actual
     * @param message
     */
    public void logAssertList(List<String> expected, List<String> actual, String message) {
        try {
            Assert.assertEquals(actual, expected);
        } catch (Exception e) {
            failedWithMessage(e.toString());
        }
    }


    /**
     * Add log step for logger and Extent report3x.
     *
     * @param stepName step name
     */
    public void addLogStepReportV4(String stepName) {
        logger.info(stepName);
        ExtentManagerV4.test.createNode(stepName);
    }

    public void assertTrue(boolean condition, String message) {
        try {
            Assert.assertTrue(condition, message);
        } catch (AssertionError e) {
            failedWithMessage(message + "\n" + e.toString());
        }
    }
    public void assertNull(Object e, String message){
        try{
            Assert.assertNull(e);
        } catch (AssertionError ex){
            logger.error(message);
            failedWithMessage(ex.toString());
        }
    }


    public void failedWithMessage(String message) {
        logger.error(message);
        Assert.fail(message);
    }

    public String readLogFile() {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream =
                     Files.lines(
                             Paths.get(PathConstant.logPath + Log.logName + ".log"), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n").append("<br/>"));
        } catch (IOException e) {
            log.ERROR("readLogFile: " + e.toString());
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
