package com.example.To_Do_List.unique;

import com.example.To_Do_List.unique.validator.UniqueEmailValidator;
import com.example.To_Do_List.unique.validator.UniqueUserNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Constraint(validatedBy = UniqueUserNameValidator.class)
public @interface UniqueUserName {
    String message() default "Такой логин уже используется!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
