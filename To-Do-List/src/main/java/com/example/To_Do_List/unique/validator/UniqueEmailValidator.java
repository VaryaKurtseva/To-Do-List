package com.example.To_Do_List.unique.validator;

import com.example.To_Do_List.repository.UserRepository;
import com.example.To_Do_List.unique.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserRepository userRepository;

    public UniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isBlank()) {
            return true; // Пустые значения проверяет @NotEmpty
        }
        return userRepository.findByEmail(value).isEmpty();
    }
}

