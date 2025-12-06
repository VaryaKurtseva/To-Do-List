package com.example.To_Do_List.DTO;

import com.example.To_Do_List.unique.UniqueEmail;
import com.example.To_Do_List.unique.UniqueUserName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String surname;

    private String dateOfBirthday;
    private int productivity;

    public UserDTO(String username, String email, String surname, String dateOfBirthday) {
        this.username = username;
        this.email = email;
        this.surname = surname;
        this.dateOfBirthday = dateOfBirthday;
        this.productivity = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductivity(int productivity) {
        this.productivity = productivity;
    }

    public UserDTO() {
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
    @UniqueUserName
    @NotEmpty(message = "Имя пользователя не должно быть пустым")
    @Size(min = 2, message = "Имя пользователя должно быть минимум 2 символа!")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotEmpty(message = "Фамилия пользователя не должна быть пустой")
    @Size(min = 2, message = "Фамилия пользователя должна быть минимум 2 символа!")
    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        if(!surname.isEmpty() || !surname.isBlank()) this.surname = surname;
        else throw new RuntimeException("Фамилия не может быть пустым");
    }
    @NotEmpty(message = "Дата рождения пользователя не должно быть пустой")
    @Size(min = 10, max = 10, message = "Дата рождения должно иметь ровно 10 символов!")
    public String getDateOfBirthday() {
        return this.dateOfBirthday;
    }

    public void setDateOfBirthday(String dateOfBirthday) {
        if(!dateOfBirthday.isBlank() || !dateOfBirthday.isEmpty()) this.dateOfBirthday = dateOfBirthday;
        else throw new RuntimeException("Дата рождения не может быть пустым");
    }

    public int getProductivity() {
        return this.productivity;
    }
}
