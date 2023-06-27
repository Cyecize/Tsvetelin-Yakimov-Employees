package com.cyecize.app.api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DateType {

    DD_MM_YYYY(DateTimeFormatter.ofPattern("dd-MM-yyyy"), 0),
    YYYY_MM_DD(DateTimeFormatter.ofPattern("yyyy-MM-dd"), 1);

    private final DateTimeFormatter formatter;

    private final int priority;

    public static LocalDate parse(String date) {
        if (StringUtils.trimToNull(date) == null) {
            return null;
        }
        final List<DateType> dateTypes = Arrays.stream(values())
                .sorted(Comparator.comparingInt(o -> o.priority))
                .collect(Collectors.toList());

        for (DateType dateType : dateTypes) {
            try {
                return LocalDate.parse(date, dateType.formatter);
            } catch (Exception ignored) {
            }
        }

        return null;
    }
}
