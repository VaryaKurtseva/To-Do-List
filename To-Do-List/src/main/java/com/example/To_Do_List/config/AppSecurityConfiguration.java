package com.example.To_Do_List.config;

import com.example.To_Do_List.model.UserRoles;
import com.example.To_Do_List.repository.UserRepository;
import com.example.To_Do_List.services.impl.AppUserDetailsService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Slf4j
@Configuration
public class AppSecurityConfiguration {

    private final UserRepository userRepository;


    public AppSecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
        log.info("AppSecurityConfiguration инициализирована");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SecurityContextRepository securityContextRepository)
            throws Exception {

        http
                // Настройка доступа к URL
                .authorizeHttpRequests(authorize -> authorize
                        // Публичные страницы (доступны всем)
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/","/home", "/register", "/login", "/login-error").permitAll()

                        // Доступ только для ADMIN
                        .requestMatchers("/users/all", "/users/*/userDelete", "/users/new",
                                "/tasks/all","/tasks/*/deleteTask").hasRole(UserRoles.ADMIN.name())


                       .requestMatchers("/users/*/userTasks", "/users/*", "/tasks/*", "/tasks/new/*", "/tasks/*/doneTaskId").hasRole(UserRoles.USER.name())

                        // Остальные страницы требуют аутентификации
                        .anyRequest().authenticated()
                )
                // Настройка формы входа
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/users/profile", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                // Настройка выхода
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                // Репозиторий контекста безопасности
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(securityContextRepository)
                );

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AppUserDetailsService(userRepository);
    }
}

