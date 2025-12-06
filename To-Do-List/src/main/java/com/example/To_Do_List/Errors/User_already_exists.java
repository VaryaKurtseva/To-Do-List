package com.example.To_Do_List.Errors;

public class User_already_exists extends RuntimeException {

    public User_already_exists(String message) {
        super(message);
    }
}
