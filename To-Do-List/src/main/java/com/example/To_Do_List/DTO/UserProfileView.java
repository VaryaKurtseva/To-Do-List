package com.example.To_Do_List.DTO;

import com.example.To_Do_List.unique.UniqueEmail;
import com.example.To_Do_List.unique.UniqueUserName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserProfileView {
    private String username;
    private String email;

    private String surname;
    private String dateOfBirthday;
    private String role;

    public UserProfileView(String username, String email, String surname, String dateOfBirthday) {
        this.username = username;
        this.email = email;

        this.surname = surname;
        this.dateOfBirthday = dateOfBirthday;
        this.role = "USER";
    }
    @NotEmpty(message = "Дата рождения пользователя не должно быть пустой")
    @Size(min = 10, max = 10, message = "Дата рождения должно иметь ровно 10 символов!")
    public String getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(String dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }
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
    @NotEmpty(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @NotEmpty(message = "Фамилия пользователя не должна быть пустой")
    @Size(min = 2, message = "Фамилия пользователя должна быть минимум 2 символа!")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
