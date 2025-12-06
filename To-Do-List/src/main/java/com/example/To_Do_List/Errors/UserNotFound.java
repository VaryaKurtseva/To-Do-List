package com.example.To_Do_List.Errors;

public class UserNotFound extends RuntimeException {

    public UserNotFound(String message) {
        super(message);
    }


}
