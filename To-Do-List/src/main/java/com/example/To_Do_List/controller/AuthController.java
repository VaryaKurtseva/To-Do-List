package com.example.To_Do_List.controller;

import com.example.To_Do_List.DTO.UserRegistrationDto;
import com.example.To_Do_List.Errors.User_already_exists;
import com.example.To_Do_List.services.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class AuthController {

    private final AuthService authService;
    private final UserController userController;

    public AuthController(AuthService authService, UserController userController) {
        this.authService = authService;
        this.userController = userController;
        log.info("AuthController инициализирован");
    }

    @ModelAttribute("userRegistrationDto")
    public UserRegistrationDto initForm() {
        return new UserRegistrationDto();
    }

    @GetMapping("/register")
    public String register() {
        log.debug("Отображение страницы регистрации");
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid UserRegistrationDto userRegistrationDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        log.debug("Обработка регистрации пользователя: {}",
                userRegistrationDto.getUsername());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации при регистрации: {}",
                    bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("userRegistrationDto",
                    userRegistrationDto);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userRegistrationDto",
                    bindingResult);

            return "redirect:/register";
        }

        try {
            this.authService.register(userRegistrationDto);
            log.info("Пользователь успешно зарегистрирован: {}", userRegistrationDto.getUsername());

            redirectAttributes.addFlashAttribute("successMessage",
                    "Регистрация успешна! Теперь вы можете войти в систему.");
            return "redirect:/login";

        } catch (User_already_exists e) {
            log.warn("Попытка регистрации с существующими данными: {}", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String login() {
        log.debug("Отображение страницы входа");
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
            String username,
            RedirectAttributes redirectAttributes) {

        log.warn("Неудачная попытка входа для пользователя: {}", username);
        redirectAttributes.addFlashAttribute(
                UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY,
                username);
        redirectAttributes.addFlashAttribute("badCredentials", true);

        return "redirect:/users/login";
    }


}




