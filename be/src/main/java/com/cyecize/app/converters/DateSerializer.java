package com.cyecize.app.converters;

import com.cyecize.summer.common.annotations.Component;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateSerializer extends StdConverter<LocalDate, String> {

    @Override
    public String convert(LocalDate value) {
        return value.format(DateTimeFormatter.ISO_DATE);
    }
}
