package com.example.To_Do_List.services;

import com.example.To_Do_List.DTO.TaskDTO;
import com.example.To_Do_List.DTO.UserDTO;
import com.example.To_Do_List.Errors.TaskNotFoundErrors;
import com.example.To_Do_List.Errors.UserNotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.*;

public interface TaskServices {
    List<TaskDTO> findAllTask();
    TaskDTO findById(Long id);
    TaskDTO findByNameTask(String nameTask) throws TaskNotFoundErrors;
    TaskDTO findByTextOfTask(String textOfTask) throws TaskNotFoundErrors;

    TaskDTO createNewTask(TaskDTO task, UserDTO userDTO) throws UserNotFound;
    boolean isDoneTask(Long id) throws TaskNotFoundErrors;
    void doneTask(Long id) throws TaskNotFoundErrors;
    void upDateDeadline(Long id, String newDate);

    void setStatus(String status, Long id) throws TaskNotFoundErrors;
    List<TaskDTO> taskByUserId(Long id) throws UserNotFound;
    LinkedHashMap<String, TaskDTO> scheduleReminders();

    Page<TaskDTO> searTasks(String search, Pageable pageable);
    Page<TaskDTO> allTasksPaginated(Pageable pageable);
    void deleteById(Long id);
}
