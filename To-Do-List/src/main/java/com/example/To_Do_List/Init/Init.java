package com.example.To_Do_List.Init;

import com.example.To_Do_List.model.Role;
import com.example.To_Do_List.model.User;
import com.example.To_Do_List.model.UserRoles;
import com.example.To_Do_List.repository.UserRepository;
import com.example.To_Do_List.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Init implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword;

    public Init(UserRepository userRepository,
                UserRoleRepository userRoleRepository,
                PasswordEncoder passwordEncoder,
                @Value("${app.default.password}") String defaultPassword) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
        log.info("Init компонент инициализирован");
    }

    @Override
    public void run(String... args) {
        log.info("Запуск инициализации начальных данных");
        initRoles();
        initUsers();
        log.info("Инициализация начальных данных завершена");
    }

    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            log.info("Создание ролей");

            Role userRole = new Role(UserRoles.USER);
            Role moderatorRole = new Role(UserRoles.MODERATOR);
            Role adminRole = new Role(UserRoles.ADMIN);

            userRoleRepository.saveAll(List.of(userRole, moderatorRole, adminRole));
            log.info("Роли созданы: USER, MODERATOR, ADMIN");
        } else {
            log.info("Роли уже существуют");
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            log.info("Создание тестовых пользователей");
            initAdmin();
            initModerator();
            initNormalUser();
            log.info("Тестовые пользователи созданы");
        } else {
            log.info("Пользователи уже существуют");
        }
    }

    private void initAdmin() {
        var adminRole = userRoleRepository
                .findRoleByName(UserRoles.ADMIN)
                .orElseThrow();
        var moderatorRole = userRoleRepository
                .findRoleByName(UserRoles.MODERATOR)
                .orElseThrow();
        var userRole = userRoleRepository
                .findRoleByName(UserRoles.USER)
                .orElseThrow();

        User admin = new User(
                "Admin",
                passwordEncoder.encode(defaultPassword),
                "admin@example.com",
                "Admin " ,
                "21.12.2005"
        );
        admin.setRoles(List.of(adminRole, moderatorRole, userRole));

        userRepository.save(admin);
        log.info("Создан администратор: " + admin.getUsername());
    }

    private void initModerator() {
        var moderatorRole = userRoleRepository
                .findRoleByName(UserRoles.MODERATOR)
                .orElseThrow();
        var userRole = userRoleRepository
                .findRoleByName(UserRoles.USER)
                .orElseThrow();

        User moderator = new User(
                "Moderator",
                passwordEncoder.encode(defaultPassword),
                "moderator@example.com",
                "Moderator",
                "21.12.2005"
        );
        moderator.setRoles(List.of(moderatorRole, userRole));

        userRepository.save(moderator);
        log.info("Создан модератор: " + moderator.getUsername());
    }

    private void initNormalUser() {
        var userRole = userRoleRepository
                .findRoleByName(UserRoles.USER)
                .orElseThrow();

        User user = new User(
                "User",
                passwordEncoder.encode(defaultPassword),
                "user@example.com",
                "Normal ",
                "21.12.2005"
        );
        user.setRoles(List.of(userRole));

        userRepository.save(user);
        log.info("Создан обычный пользователь: " + user.getUsername());
    }
}
