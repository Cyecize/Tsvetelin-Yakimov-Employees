package com.cyecize.app.converters;

import com.cyecize.app.api.util.DateType;
import com.cyecize.summer.common.annotations.Component;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.LocalDate;

@Component
public class DateDeserializer extends StdConverter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        return DateType.parse(source);
    }
}
