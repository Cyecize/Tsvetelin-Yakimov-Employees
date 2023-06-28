package com.cyecize.app.api.validators;

import com.cyecize.summer.areas.validation.exceptions.ErrorDuringValidationException;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import com.cyecize.summer.utils.ReflectionUtils;
import java.lang.reflect.Field;
import java.time.LocalDate;

@Component
public class DateIsAfterFieldValidator implements ConstraintValidator<DateIsAfterField, Object> {

    private static final String MATCHING_FIELD_NOT_FOUND_FORMAT = "Field \"%s\" was not found";

    private String fieldName;

    @Override
    public void initialize(DateIsAfterField constraintAnnotation) {
        this.fieldName = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object fieldVal, Object bindingModel) {
        if (fieldVal == null) {
            return true;
        }

        final Field matchingField = ReflectionUtils.getAllFieldsRecursively(bindingModel.getClass())
                .stream()
                .filter(f -> f.getName().equals(this.fieldName))
                .findFirst().orElse(null);

        if (matchingField == null) {
            throw new ErrorDuringValidationException(
                    String.format(MATCHING_FIELD_NOT_FOUND_FORMAT, this.fieldName));
        }

        matchingField.setAccessible(true);

        Object matchingVal;
        try {
            matchingVal = matchingField.get(bindingModel);
        } catch (IllegalAccessException e) {
            throw new ErrorDuringValidationException(e.getMessage(), e);
        }

        if (matchingVal == null) {
            return true;
        }

        return ((LocalDate) fieldVal).isAfter((LocalDate) matchingVal);
    }

}
