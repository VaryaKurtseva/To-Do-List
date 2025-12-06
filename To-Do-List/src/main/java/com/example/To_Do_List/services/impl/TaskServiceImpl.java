package com.example.To_Do_List.services.impl;


import com.example.To_Do_List.DTO.TaskDTO;
import com.example.To_Do_List.DTO.UserDTO;
import com.example.To_Do_List.Errors.TaskAlreadyDone;
import com.example.To_Do_List.Errors.TaskAlreadyExists;
import com.example.To_Do_List.Errors.TaskNotFoundErrors;
import com.example.To_Do_List.Errors.UserNotFound;
import com.example.To_Do_List.model.Task;

import com.example.To_Do_List.model.User;
import com.example.To_Do_List.repository.TaskRepository;
import com.example.To_Do_List.repository.UserRepository;
import com.example.To_Do_List.services.TaskServices;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service

public class TaskServiceImpl implements TaskServices {
    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ModelMapper modelMapper;


    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        log.info("TaskServiceImpl успешно создан");
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value="tasks",key = "'all'", unless = "#result == null")
    public List<TaskDTO> findAllTask() {
        log.debug("Поиск всех задач");
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> taskDTOS = new ArrayList<>();
        for(Task ts: tasks){
            taskDTOS.add(convertToDTO(ts));
        }
        return taskDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO findById(Long id) {
        log.debug("Поиск задачи c id {}", id);
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()) return convertToDTO(task.get()) ;
        else throw new RuntimeException("Задача по такому ID отсустует");
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO findByNameTask(String nameTask)  {
        log.debug("Поиск задачи c nameTask {}", nameTask);
        Task task = taskRepository.findByNameTask(nameTask);
        if(task != null) return convertToDTO(task);
        else throw new TaskNotFoundErrors(String.format("Задачка с таким именем %s отсуствует", nameTask));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO findByTextOfTask(String textOfTask)  {
        log.debug("Поиск задачи c texOfTask {}", textOfTask);
        Task task = taskRepository.findByTextOfTask(textOfTask);
        if(task != null) return convertToDTO(task);
        else throw new TaskNotFoundErrors(String.format("Задачка с таким описанием %s отсуствует", textOfTask));
    }


    @Override
    @Transactional // данные при ошибке не попадают в бд, при ошибке ничего не сохраняем а откатываемся назад
    @CacheEvict(cacheNames = {"tasks","task", "tasksByUser"}, allEntries = true)
    public void deleteById(Long id) {
        log.debug("Удаление задачи c id {}", id);
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(taskOptional.isPresent()){
            Task task = taskOptional.get();
            for(User user: new ArrayList<>(task.getUsers())){
                task.getUsers().remove(user);
                user.getTasks().remove(task);
            }
            taskRepository.save(task);

            taskRepository.deleteById(id);
            log.info("Успешное удаление задачи c id {}", id);
        }
        else throw new TaskNotFoundErrors(String.format("Задачка по такому ID %s + отсуствует", String.valueOf(id)));
    }

    @Override
    @CacheEvict(value = "tasks", allEntries = true)
    public TaskDTO createNewTask(TaskDTO taskDTO, UserDTO userDTO){
        log.debug("Создание новой задачи {}", taskDTO);
        if(taskRepository.findByNameTask(taskDTO.getNameTask()) != null) throw new TaskAlreadyExists(String.format("Задача с таким именем %s уже существует", taskDTO.getNameTask()));
        else {
            Task task = new Task();
            task.setNameTask(taskDTO.getNameTask());
            task.setTextOfTask(taskDTO.getTextOfTask());
            task.setDeadline(taskDTO.getDeadline());
            task.setAdditionalInfo(taskDTO.getAdditionalInfo());

            // Установите статус
            if (taskDTO.getStatus() != null) {
                task.setStatus(taskDTO.getStatus());
            }

            if (taskDTO.getUserId() == null) {
                throw new UserNotFound("ID пользователя не может быть пустым");
            }

            Optional<User> userOptional = userRepository.findById(taskDTO.getUserId());
            if (!userOptional.isPresent()) {
                throw new UserNotFound(String.format("Пользователь с таким ID %s отсутствует", taskDTO.getUserId()));
            }

            User user = userOptional.get();
            task.addUsers(user);
            Task savedTask = taskRepository.save(task);
            userRepository.save(user);
            TaskDTO rezDTO = convertToDTO(savedTask);
            log.info("Успешное создание новой задачи {}", savedTask.getNameTask());
            return rezDTO;
        }

    }
    public TaskDTO convertToDTO(Task task){
        log.debug("Конвертируем в дто задачку и создаем пользователя");

        if(task.getUsers() != null && !task.getUsers().isEmpty()){

            User user = task.getUsers().get(0);
            UserDTO rezUserDto = new UserDTO();
            rezUserDto.setUsername(user.getUsername());
            rezUserDto.setSurname(user.getSurname());
            rezUserDto.setDateOfBirthday(user.getDateOfBirthday());
            rezUserDto.setProductivity(user.getProductivity());
            log.info("Create UserDTO {}", rezUserDto);
            TaskDTO taskDTO = modelMapper.map(task,TaskDTO.class);
            taskDTO.setUser(rezUserDto);
            taskDTO.setUserId(user.getId());
            log.info("Успешная конвертация в дто задачку и создаем пользователя");

            return taskDTO;
        }else {
            log.warn("No users found for task {}" , task.getNameTask());
            return modelMapper.map(task,TaskDTO.class);
        }


    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDoneTask(Long id){
        log.debug("Проверка выполнена ли задача с таким id {}", id);
       Optional<Task> task = taskRepository.findById(id);
       if(task.isPresent()){
           Task taskByName = taskRepository.findByNameTask(task.get().getNameTask());
           if(taskByName != null) return task.get().getStatus().equals("Задача выполнена");
           else throw new TaskNotFoundErrors(String.format("Задачка по такому с таким ID %s и именем %s отсуствует", String.valueOf(id), taskByName.getNameTask()));
       }else throw new TaskNotFoundErrors(String.format("Задачка по такому с таким ID %s отсуствует", String.valueOf(id)));
    }

    @Override
    @Transactional
    public void doneTask(Long id) {
        log.debug("Выполнение задачи с таким id {}", id);
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && (task.get().getNameTask() != null)){
            String oldStatus = task.get().getStatus();
            if(task.get().getStatus().equals("done")) throw new TaskAlreadyDone("Задача уже выполнена");
            else{
                task.get().setStatus("done");
                List<User> userList = task.get().getUsers();
                for(User us : userList){
                    us.incrementProductivity();
                    userRepository.save(us);

                }
                log.info("Успешное выполнение задачи с таким id {}", id);
                taskRepository.save(task.get());
            }

        }else throw new TaskNotFoundErrors(String.format("Задачка по такому ID %s + отсуствует", String.valueOf(id)));
    }

    @Override
    public void upDateDeadline(Long id, String newDate) {
        log.debug("Обнавление дедлайна на {}" ,newDate);
        if(taskRepository.findById(id).isPresent()){
            Task task = taskRepository.findById(id).get();
            task.setDeadline(newDate);
            taskRepository.save(task);
            log.info("Успешное обнавление дедлайна на {}" ,newDate);
        }
    }


    @Override
    @Transactional
    public void setStatus(String status, Long id) {
        log.debug("Установка нового статуса {}" ,status);
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setStatus(status);
            taskRepository.save(task);
            log.info("Успешная установка нового статуса {}" ,status);
        }else throw new TaskNotFoundErrors(String.format("Задачка по такому ID %s + отсуствует", String.valueOf(id)));

    }

    @Override

    public List<TaskDTO> taskByUserId(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            List<TaskDTO> taskDTOS = new ArrayList<>();
            for(Task ts: user.get().getTasks()){
                taskDTOS.add(convertToDTO(ts));

            }
            log.info("Успешный поиск задач пользователя {}", id);
            return taskDTOS;

        }else throw new UserNotFound(String.format("Пользователь с таким именем %s отсуствует", id));
    }

    @Override
    public LinkedHashMap<String, TaskDTO> scheduleReminders() {
        List<TaskDTO> allTask = this.findAllTask();
        log.info("Получили все задачи: " + allTask);
        List<TaskDTO> sortdedTask = allTask.stream().sorted(
                Comparator.comparing(taskDTO -> {
                    return LocalDate.parse(taskDTO.getDeadline());})).collect(Collectors.toList());
        LinkedHashMap<String, TaskDTO> schedule = new LinkedHashMap<>();
        LocalDate dateNow = LocalDate.now();
        for (TaskDTO taskDTO : sortdedTask){
            LocalDate taskDate = LocalDate.parse(taskDTO.getDeadline());
            long days = ChronoUnit.DAYS.between(dateNow, taskDate);
            long years = ChronoUnit.YEARS.between(dateNow,taskDate);
            if(years < 1){
                if(days < 0){
                    schedule.put("Дедлайн просрочен", taskDTO);
                } else if (days <= 2) {
                    schedule.put("Дедлайн скоро закончится", taskDTO);
                } else if (days < 5) {
                    schedule.put("Готовтесь к выполнению данной задачи ", taskDTO);
                } else if (days <=10) {
                    schedule.put("Задача на подходе", taskDTO);
                }else schedule.put("Задачу надо выполнить не скоро", taskDTO);

            }else schedule.put("Не срочно", taskDTO);
        }
        return schedule;

    }

    @Override
    public Page<TaskDTO> searTasks(String search, Pageable pageable) {
        log.info("Поиск задач: '{}', страница: {}, размер: {}",
                search, pageable.getPageNumber(), pageable.getPageSize());

        // Используем исправленный метод репозитория
        Page<Task> taskPage = taskRepository.findByNameTaskContainingIgnoreCaseOrTextOfTaskContainingIgnoreCase(
                search, search, pageable);

        log.info("Найдено задач: {}", taskPage.getTotalElements());

        return taskPage.map(this::convertToDTO);
    }

    @Override
    public Page<TaskDTO> allTasksPaginated(Pageable pageable) {
        log.debug("Получение задач с пагинацией: {}, размер {}",pageable.getPageNumber(),pageable.getPageSize());
        return taskRepository.findAll( pageable)
                .map(this::convertToDTO);
    }
}
