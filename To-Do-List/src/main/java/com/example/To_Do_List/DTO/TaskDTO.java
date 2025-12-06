package com.example.To_Do_List.DTO;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class TaskDTO implements Serializable {
    private Long id;
    private String nameTask;
    private String textOfTask;
    private String deadline;
    private String additionalInfo;
    private String status;
    private Long userId;

    private UserDTO user;

    public TaskDTO(Long id,String nameTask, String textOfTask, String deadline, String status) {
        this.id=id;
        this.setNameTask(nameTask);
        this.setTextOfTask(textOfTask);
        this.setDeadline(deadline);
        this.setStatus(status);

    }

    public TaskDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @NotEmpty(message = "Статус не должно быть пустым")
    @Size(min = 4, max = 10, message = "Статус должен быть от 4 до 10 символов!")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NotEmpty(message = "Имя задачи не должно быть пустым")
    @Size(min = 5, max = 25, message = "Имя задачи должно быть от 5 до 25 символов!")
    public String getNameTask() {
        return this.nameTask;
    }

    public void setNameTask(String nameTask) {
        if(!nameTask.isBlank() || !nameTask.isEmpty()) this.nameTask = nameTask;
        else throw new RuntimeException("Имя задачи  не может быть пустым");
    }
    @NotEmpty(message = "Описание задачи не должно быть пустым")
    @Size(min = 10, max = 60, message = "Описание задачи должно быть от 10 до 60 символов!")
    public String getTextOfTask() {
        return this.textOfTask;
    }

    public void setTextOfTask(String textOfTask) {
        if(!textOfTask.isBlank() || !textOfTask.isEmpty()) this.textOfTask = textOfTask;
        else throw new RuntimeException("Текст задачи не может быть пустым");
    }
    @NotEmpty(message = "Дедлайн задачи не должно быть пустым")
    @Size(min = 10, max = 10, message = "Дедлайн задачи должен иметь ровно 10 символов!")
    public String getDeadline() {
        return this.deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Size(min = 3, max = 50, message = "Дополнительная информация должна быть от 3 до 50 символов!")
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
