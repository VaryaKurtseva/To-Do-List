package com.example.To_Do_List.unique.validator;


import com.example.To_Do_List.repository.UserRepository;
import com.example.To_Do_List.unique.UniqueUserName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {
    private final UserRepository userRepository;

    public UniqueUserNameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if ( username == null || username.isBlank()) {
            return true; // Пустые значения проверяет @NotEmpty
        }
        return userRepository.findByUsername(username) == null;
    }
}
