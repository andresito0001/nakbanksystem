package main.java.util;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeZone {
    public static String getDateZoneCaracas() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("America/Caracas"));
        String formattedDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return formattedDateTime.trim();
    }

    public static String getTimeZoneCaracas() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("America/Caracas"));
        String formattedTime = zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        return formattedTime;
    }

    public static String obtenerHoraCaracas() {
        LocalTime nowInCaracas = LocalTime.now(ZoneId.of("America/Caracas"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = nowInCaracas.format(formatter);

        return formattedTime.trim();
    }

}
