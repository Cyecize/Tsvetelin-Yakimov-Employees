package com.cyecize.app.api.csv;

import com.cyecize.app.error.ApiException;
import com.cyecize.ioc.annotations.Service;
import com.cyecize.summer.areas.routing.interfaces.UploadedFile;
import com.cyecize.summer.areas.routing.utils.PrimitiveTypeDataResolver;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CSVParserImpl implements CSVParser {

    private final Map<Class<?>, StdConverter<String, ?>> converters;

    private final PrimitiveTypeDataResolver primitiveTypeDataResolver = new PrimitiveTypeDataResolver();

    @SuppressWarnings("unchecked")
    public CSVParserImpl(List<StdConverter> converters) {
        this.converters = converters.stream()
                .collect(Collectors.toMap(StdConverter::getClass, c -> c));
    }

    @Override
    public <T> List<T> parse(UploadedFile csv, Class<T> type) {
        final FieldSupplierPair[] fields = this.getFieldSupplierPairs(type);

        final BufferedReader br = new BufferedReader(
                new InputStreamReader(csv.getUploadedFile().getInputStream())
        );

        String line;
        final List<T> result = new ArrayList<>();
        try {
            // Read first line to skip the header.
            br.readLine();
            while ((line = br.readLine()) != null) {
                final String[] data = line.split("\\s*,\\s*");
                final T dto = this.createInstance(type);

                for (int fieldInd = 0; fieldInd < fields.length; fieldInd++) {
                    if (data.length - 1 < fieldInd) {
                        continue;
                    }

                    final FieldSupplierPair field = fields[fieldInd];
                    field.getField().setAccessible(true);

                    final Object value = field.getSupplier().apply(data[fieldInd]);
                    try {
                        field.getField().set(dto, value);
                    } catch (IllegalAccessException e) {
                        log.error("Could not set value {} to field {}!",
                                value,
                                field.getField().getName(),
                                e
                        );
                        throw new ApiException("Internal error occurred during CSV parse!");
                    }
                }

                result.add(dto);
            }

            return result;
        } catch (IOException e) {
            log.error("Error while parsing CSV file '{}'!", csv.getUploadedFile().getFileName(), e);
            throw new ApiException("Error while parsing CSV file!");
        }
    }

    private <T> T createInstance(Class<T> cls) {
        try {
            return cls.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            log.error("Could not create instance of {}!", cls.getName(), e);
            throw new ApiException("Internal error occurred during CSV parse!");
        }
    }

    private FieldSupplierPair[] getFieldSupplierPairs(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .map(field -> {
                    final Function<String, ?> supplier;
                    final JsonDeserialize annotation = this.deepSearchJsonDeserialize(
                            field.getDeclaredAnnotations()
                    );
                    if (annotation != null) {
                        supplier = (str) -> this.converters
                                .get(annotation.converter()).convert(str);
                    } else {
                        supplier = str -> this.primitiveTypeDataResolver
                                .resolve(field.getType(), str);
                    }

                    return new FieldSupplierPair(field, supplier);
                })
                .toArray(FieldSupplierPair[]::new);
    }

    private JsonDeserialize deepSearchJsonDeserialize(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == JsonDeserialize.class) {
                return (JsonDeserialize) annotation;
            }

            final JsonDeserialize inner = (JsonDeserialize) Arrays.stream(
                            annotation.annotationType().getDeclaredAnnotations())
                    .filter(a -> a.annotationType() == JsonDeserialize.class)
                    .findFirst().orElse(null);

            if (inner != null) {
                return inner;
            }
        }

        return null;
    }

    @Data
    @AllArgsConstructor
    static class FieldSupplierPair {

        private Field field;
        private Function<String, ?> supplier;
    }
}
