package com.deni.mallcoursework.infrastructure.password;

import com.deni.mallcoursework.util.Constants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=ConfirmPasswordValidator.class)
@Documented
public @interface ConfirmPassword {
    String message() default Constants.PASSWORDS_DONT_MATCH;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
