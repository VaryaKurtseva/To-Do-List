package com.example.To_Do_List.services.impl;

import com.example.To_Do_List.DTO.UserRegistrationDto;
import com.example.To_Do_List.Errors.UserNotFound;
import com.example.To_Do_List.Errors.User_already_exists;
import com.example.To_Do_List.model.Role;
import com.example.To_Do_List.model.User;
import com.example.To_Do_List.model.UserRoles;
import com.example.To_Do_List.repository.UserRepository;
import com.example.To_Do_List.repository.UserRoleRepository;
import com.example.To_Do_List.services.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    public void register(UserRegistrationDto registrationDTO) {
        // Проверка совпадения паролей


        // Проверка уникальности email
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new User_already_exists("Email уже используется!");
        }
        // Проверка уникальности username
        if (userRepository.findByUsername(registrationDTO.getUsername()) != null) {
            throw new User_already_exists("Логин уже используется!");
        }

        // Получаем роль USER
        var userRole = userRoleRepository.findRoleByName(UserRoles.USER)
                .orElseThrow(() -> new RuntimeException("Такая роль отсуствует"));

        // Создаём пользователя
        User user = new User(
        );
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEmail(registrationDTO.getEmail());
        user.setUsername(registrationDTO.getUsername());
        user.setSurname(registrationDTO.getSurname());
        user.setDateOfBirthday(registrationDTO.getDateOfBirthday());


        // Назначаем роль
        user.setRoles(List.of(userRole));



        // Сохраняем
        userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user != null) return user;
        else throw new UserNotFound(username + " не найден!");
    }
}

