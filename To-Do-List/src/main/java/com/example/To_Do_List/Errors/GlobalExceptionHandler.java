package com.example.To_Do_List.Errors;

import com.example.To_Do_List.unique.validator.UniqueEmailValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String UserNotFound(UserNotFound ex, Model model) {
        log.warn("Пользователь не найден: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Пользователь не найдена");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "404");
        return "error/custom-error";
    }
    @ExceptionHandler(TaskNotFoundErrors.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String TaskNotFound(TaskNotFoundErrors ex, Model model) {
        log.warn("Задача не найдена: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Задача не найдена");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", " 404");
        return "error/custom-error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        log.warn("Некорректные данные: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Некорректные данные");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", " 400");
        return "error/custom-error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Внутренняя ошибка сервера", ex);
        model.addAttribute("errorTitle", "Внутренняя ошибка сервера");
        model.addAttribute("errorMessage", "Произошла непредвиденная ошибка");
        model.addAttribute("errorCode", " 500");
        return "error/custom-error";
    }
    @ExceptionHandler(User_already_exists.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String UserAlreadyExists(User_already_exists ex, Model model) {
        log.warn("Пользователь уже существует: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Пользователь уже существует");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", " 406");
        return "error/custom-error";
    }
    @ExceptionHandler(TaskAlreadyExists.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String TaskAlreadyExists(TaskAlreadyExists ex, Model model) {
        log.warn("Задача уже существует: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Задача уже существует");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", " 406");
        return "error/custom-error";
    }
    @ExceptionHandler(TaskAlreadyDone.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String TaskAlreadyDone(TaskAlreadyDone ex, Model model) {
        log.warn("Задача уже сделана: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Задача уже сделана");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", " 406");
        return "error/custom-error";
    }

}


