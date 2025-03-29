package org.example.flight_booking.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class DateTimeUtil {
    /**
     * Converts a string representation of a time to a ZonedDateTime object
     *
     * @param time     the time to convert
     * @param timezone the timezone of the time
     * @return the ZonedDateTime object
     */
    public static ZonedDateTime convertToZonedTime(String time, String timezone) {
        LocalDateTime localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        return localDateTime.atZone(ZoneId.of(timezone));
    }
}
