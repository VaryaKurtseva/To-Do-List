package com.example.To_Do_List.model;

import jakarta.persistence.*;

import java.util.ArrayList;

import java.util.List;

@Entity
@Table(name = "task")
public class Task extends BaseEntity{

    @Column(name = "nameTask")
    private String nameTask;
    @Column(name = "textOfTask")
    private String textOfTask;
    @Column(name = "deadline")
    private String deadline;
    @Column(name = "additionalInfo")
    private String additionalInfo;
    @Column(name = "status")
    private String status;
    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();


    public Task(Long id,String nameTask, String textOfTask, String deadline, String status) {
        this.setId(id);
        this.setNameTask(nameTask);
        this.setTextOfTask(textOfTask);
        this.setDeadline(deadline);
        this.setStatus(status);

    }

    public Task() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUsers(User user) {
        users.add(user);
        user.getTasks().add(this);
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public void setId(Long id) {
        if(id > 0) this.id = id;
        else throw new RuntimeException("ID не может быть отрицательным");
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        if(!nameTask.isBlank() || !nameTask.isEmpty()) this.nameTask = nameTask;
        else throw new RuntimeException("Имя задачи  не может быть пустым");
    }

    public String getTextOfTask() {
        return textOfTask;
    }

    public void setTextOfTask(String textOfTask) {
        if(!textOfTask.isBlank() || !textOfTask.isEmpty()) this.textOfTask = textOfTask;
        else throw new RuntimeException("Текст задачи не может быть пустым");
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }


}
