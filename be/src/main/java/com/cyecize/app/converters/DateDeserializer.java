package com.cyecize.app.converters;

import com.cyecize.summer.common.annotations.Component;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateDeserializer extends StdConverter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        try {
            return LocalDate.parse(source, DateTimeFormatter.ISO_DATE);
        } catch (Exception ignored) {
        }

        return null;
    }
}
