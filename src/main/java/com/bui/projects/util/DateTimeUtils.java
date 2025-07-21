package com.bui.projects.util;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@UtilityClass
public class DateTimeUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Long convertDateTimeToTimestamp(LocalDateTime localDateTime) {
        if (Objects.nonNull(localDateTime)) {
            OffsetDateTime odt = localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
            return localDateTime.toInstant(odt.getOffset()).toEpochMilli();
        } else {
            return null;
        }
    }

    public LocalDateTime convertTimestampToDateTime(Long timestampLong) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampLong), ZoneId.systemDefault());
    }

    public String convertDateToString(LocalDate localDate) {
        return localDate.format(FORMATTER);
    }

    public LocalDate convertStringToDate(String timestamp) {
        return LocalDate.parse(timestamp, FORMATTER);
    }
}
