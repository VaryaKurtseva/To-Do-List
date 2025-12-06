package com.example.To_Do_List.Errors;

public class TaskAlreadyDone extends RuntimeException{
    public TaskAlreadyDone(String ex){super(ex);}
}
