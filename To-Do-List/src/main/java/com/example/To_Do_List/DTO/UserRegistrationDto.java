package com.example.To_Do_List.DTO;

import com.example.To_Do_List.unique.UniqueEmail;
import com.example.To_Do_List.unique.UniqueUserName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

public class UserRegistrationDto {
    private String username;
    private String email;
    private String password;

    private String role;

    private String surname;
    private String dateOfBirthday;

    public UserRegistrationDto() {
    }

    // Геттеры и сеттеры
    @UniqueUserName
    @NotEmpty(message = "Имя пользователя не должно быть пустым")
    @Size(min = 2, message = "Имя пользователя должно быть минимум 2 символа!")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @UniqueEmail
    @NotEmpty(message = "Почта не должна быть пустой")
    @Email(message = "Некорректный формат email")
    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }
    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 8,max=10,message = "Пароль должен быть от 8 до 10!")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    @NotEmpty(message = "Фамилия пользователя не должна быть пустой")
    @Size(min = 3, max = 25, message = "Фамилия пользователя должно быть от 3 до 25 символов!")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    @NotEmpty(message = "Дата рождения пользователя не должно быть пустым")
    @Size(min = 10, max = 10, message = "Дата рождения должно иметь ровно 10 символов!")
    public String getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(String dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }
}