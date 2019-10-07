package helper;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class CommonMethods {
    public static String getRandomString(int length) {
        char[] chars = "QWERTYUIPASDFGHJKLZXCVBNM".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output.trim();
    }

    /**
     * @param date        MM/dd/yyyy
     *                    Example: 10/04/2019
     *                    Result: October 2019
     */
    public static String convertMonthYear(String date) {
        String month = date.split("/")[0];
        String monthString = new DateFormatSymbols().getMonths()[Integer.parseInt(month) -1];
        return monthString + " " + date.split("/")[2];
    }

    public static String getCurrentDate(String format) {
        String date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        date = formatter.format(calendar.getTime());
        return date;
    }

    public static String getTomorrowDate(String format) {
        String date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        date = formatter.format(calendar.getTime());
        return date;
    }
}
