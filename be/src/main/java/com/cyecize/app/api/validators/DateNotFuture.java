package com.cyecize.app.api.validators;

import com.cyecize.summer.areas.validation.annotations.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DateNotFutureConstraint.class)
public @interface DateNotFuture {
    String message() default "Date is in the future";
}
