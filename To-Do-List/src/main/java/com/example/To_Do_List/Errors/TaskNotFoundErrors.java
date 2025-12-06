package com.example.To_Do_List.Errors;



public class TaskNotFoundErrors extends RuntimeException {

    public TaskNotFoundErrors(String message) {
        super(message);
    }
}
