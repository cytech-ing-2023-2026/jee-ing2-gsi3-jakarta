package fr.cyu.jee.util;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {

    public static DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatDuration(Duration duration) {
        int hours = duration.toHoursPart();
        int minutes = duration.toMinutesPart();

        String hoursStr = hours < 10 ? "0" + hours : String.valueOf(hours);
        String minutesStr = minutes < 10 ? "0" + minutes : String.valueOf(minutes);

        return hoursStr + ":" + minutesStr;
    }
}