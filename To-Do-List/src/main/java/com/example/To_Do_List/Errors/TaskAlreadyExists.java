package com.example.To_Do_List.Errors;

public class TaskAlreadyExists extends RuntimeException{
    public TaskAlreadyExists(String ex){super(ex);}
}
