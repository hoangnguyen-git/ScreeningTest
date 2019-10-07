package createAnEvent;

import com.google.gson.JsonObject;
import helper.CommonMethods;
import helper.JsonUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import page.CalendarPage;
import page.LoginPage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CreateBasicEvent extends BaseTest {
    private static final String CREATE_BASIC_EVENT = "/data/basic-event.json";
    private static final String CREATE_OPTION_EVENT = "/data/option-event.json";
    private String format = "MM/dd/yyyy";

    @DataProvider(name = "createBasicEventData")
    public static Iterator<Object[]> createBasicEventData() throws FileNotFoundException {
        Iterator<Object[]> jsonData = JsonUtils.readJsonFile(CreateBasicEvent.class.getResource(CREATE_BASIC_EVENT).getPath());
        List<Object[]> returnValue = new ArrayList<>();
        while (jsonData.hasNext()) {
            Object[] jsonRow = jsonData.next();
            for (Object jsonObject : jsonRow) {
                List<Object> row = new ArrayList<>();
                row.add(((JsonObject) jsonObject).get("basicEvent").getAsJsonObject());
                returnValue.add(row.toArray());
            }
        }
        return returnValue.iterator();
    }

    @Test(description = "Verify that user can create a basic event successfully", dataProvider = "createBasicEventData")
    public void createBasicEvent(JsonObject jsonBasicEvent) {
        LoginPage loginPage = new LoginPage(driver);
        CalendarPage calendarPage = new CalendarPage(driver);
        String title = jsonBasicEvent.get("title").getAsString() + " - " + CommonMethods.getRandomString(4);
        String startDate = CommonMethods.getTomorrowDate(format);
        String startTime = jsonBasicEvent.get("startTime").getAsString();
        String endTime = jsonBasicEvent.get("endTime").getAsString();
        String endDate = CommonMethods.getTomorrowDate(format);
        String guest = jsonBasicEvent.get("guest").getAsString();
        String location = jsonBasicEvent.get("location").getAsString();
        String description = jsonBasicEvent.get("description").getAsString();

        logger.INFO("Go to calendar page");
        this.goToCalendarPage(loginPage, calendarPage);
        logger.INFO("Click on Create button");
        calendarPage.clickOnCreateButton();
        logger.INFO("Enter the event information");
        calendarPage.inputEventInformation(title, startDate, startTime, endTime, endDate, guest, location, description);
        logger.INFO("Click on Save button");
        calendarPage.clickOnSaveButton();

        logger.INFO("Verify Event is created successfully");
        softAssertions.assertThat(calendarPage.isEventIsCreated(title)).isTrue().as("Event title is" +
                "is incorrect");
        softAssertions.assertThat(calendarPage.getMonthYearEvent()).isEqualTo(CommonMethods.convertMonthYear(startDate))
                .as("Month and Year are incorrect");
        softAssertions.assertThat(calendarPage.getDateEvent()).isEqualTo(Integer.parseInt(startDate.split("/")[1]))
                .as("Date is incorrect");
        softAssertions.assertThat(calendarPage.getTimeEvent()).contains(startTime)
                .as("Time in incorrect");
        softAssertions.assertThat(calendarPage.getTimeEvent()).contains(location)
                .as("Location in incorrect");
        softAssertions.assertAll();
    }

    @DataProvider(name = "createOptionEventData")
    public static Iterator<Object[]> createOptionEventData() throws FileNotFoundException {
        Iterator<Object[]> jsonData = JsonUtils.readJsonFile(CreateBasicEvent.class.getResource(CREATE_OPTION_EVENT).getPath());
        List<Object[]> returnValue = new ArrayList<>();
        while (jsonData.hasNext()) {
            Object[] jsonRow = jsonData.next();
            for (Object jsonObject : jsonRow) {
                List<Object> row = new ArrayList<>();
                row.add(((JsonObject) jsonObject).get("optionEvent").getAsJsonObject());
                returnValue.add(row.toArray());
            }
        }
        return returnValue.iterator();
    }

    @Test(description = "Verify that user can create an option event successfully", dataProvider = "createOptionEventData")
    public void createOptionEvent(JsonObject jsonOptionEvent) {
        LoginPage loginPage = new LoginPage(driver);
        CalendarPage calendarPage = new CalendarPage(driver);
        String title = jsonOptionEvent.get("title").getAsString() + " - " + CommonMethods.getRandomString(4);
        String startDate = CommonMethods.getCurrentDate(format);
        String startTime = jsonOptionEvent.get("startTime").getAsString();
        String endTime = jsonOptionEvent.get("endTime").getAsString();
        String endDate = CommonMethods.getCurrentDate(format);
        String guest = jsonOptionEvent.get("guest").getAsString();
        String location = jsonOptionEvent.get("location").getAsString();
        String description = jsonOptionEvent.get("description").getAsString();

        logger.INFO("Go to calendar page");
        this.goToCalendarPage(loginPage, calendarPage);
        logger.INFO("Click on Create button");
        calendarPage.clickOnCreateButton();
        logger.INFO("Click on More option button");
        calendarPage.clickOnMoreOption();
        logger.INFO("Enter the event information");
        calendarPage.inputOptionEventInformation(
                title, startDate, startTime, endTime, endDate, guest, location, description);
        logger.INFO("Click on Save button");
        calendarPage.clickOnSaveButton();

        logger.INFO("Verify Event is created successfully");
        softAssertions.assertThat(calendarPage.isEventIsCreated(title)).isTrue().as("Event title is" +
                "is incorrect");
        softAssertions.assertThat(calendarPage.getMonthYearEvent()).isEqualTo(CommonMethods.convertMonthYear(startDate))
                .as("Month and Year are incorrect");
        softAssertions.assertThat(calendarPage.
                getDateEvent()).isEqualTo(Integer.parseInt(startDate.split("/")[1]))
                .as("Date is incorrect");
        softAssertions.assertThat(calendarPage.getTimeEvent()).contains(startTime)
                .as("Time in incorrect");
        softAssertions.assertThat(calendarPage.getTimeEvent()).contains(location)
                .as("Location in incorrect");
        softAssertions.assertAll();
    }

    private void goToCalendarPage(LoginPage loginPage, CalendarPage calendarPage) {
        loginPage.setEnglishLanguage();
        loginPage.enterEmail(config.getWebEnvironment().get("username"));
        loginPage.clickOnNextButton();
        loginPage.enterPassword(config.getWebEnvironment().get("password"));
        loginPage.clickOnNextButton();
        calendarPage.waitForPageIsDisplayed();
    }
}
