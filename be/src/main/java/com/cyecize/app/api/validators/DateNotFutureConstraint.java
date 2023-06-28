package com.cyecize.app.api.validators;

import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import java.time.LocalDate;

@Component
public class DateNotFutureConstraint implements ConstraintValidator<DateNotFuture, Object> {

    @Override
    public boolean isValid(Object fieldVal, Object bindingModel) {
        if (fieldVal == null) {
            return true;
        }

        return LocalDate.now().plusDays(1).isAfter((LocalDate) fieldVal);
    }

}
