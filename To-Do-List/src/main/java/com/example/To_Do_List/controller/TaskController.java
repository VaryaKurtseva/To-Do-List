package com.example.To_Do_List.controller;

import com.example.To_Do_List.DTO.TaskDTO;
import com.example.To_Do_List.DTO.UserDTO;
import com.example.To_Do_List.Errors.TaskAlreadyDone;
import com.example.To_Do_List.Errors.UserNotFound;
import com.example.To_Do_List.services.TaskServices;
import com.example.To_Do_List.services.UserServices;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskServices taskServices;
    @Autowired
    private UserServices userServices;



    @GetMapping("/all")
    public String getAllTask(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(defaultValue = "deadline") String sortBy,
                             @RequestParam(required = false) String search,
                             Model model){
        log.debug("Отображение списка всех пользователей: страница={}, размер={}, поиск={}",page,size,search);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        if (search != null && !search.trim().isEmpty()) {
            // Режим поиска
            model.addAttribute("allTasks", taskServices.searTasks(search,pageable));
            model.addAttribute("search", search);
        } else {
            // Режим пагинации

            Page<TaskDTO> userDTOPage = taskServices.allTasksPaginated(pageable);
            model.addAttribute("allTasks", userDTOPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userDTOPage.getTotalPages());
            model.addAttribute("totalItems", userDTOPage.getTotalElements());
            model.addAttribute("sortBy",sortBy);
            model.addAttribute("search", search);
        }
        return "tasksАll";
    }
    @GetMapping("/{id}/deleteTask")
    public String deleteByNameAndSurname(@PathVariable("id") Long id,

                                         RedirectAttributes redirectAttributes){
        log.debug("Запрос на удаления задачи");
        taskServices.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Задача " + id + " удалена!");
        return "redirect:/tasks/all";

    }

    @GetMapping("/new/{userId}")
    public String showTaskForm(@PathVariable Long userId, Model model) {
        log.debug("Создание задачи для пользователя с ID: {}", userId);
        TaskDTO taskDTO = new TaskDTO();
        UserDTO userDTO = new UserDTO();

        model.addAttribute("userId", userId);
        model.addAttribute("TaskDTO", taskDTO);


        return "tasksForm";
    }

    @PostMapping("/new/{userId}")
    public String addTask(@PathVariable Long userId,@Valid @ModelAttribute("TaskDTO") TaskDTO taskDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model ) throws UserNotFound {
        log.debug("Обработка POST запроса на добавление задачи");

        model.addAttribute("userId", userId);
        if(bindingResult.hasErrors()) return "tasksForm";
        UserDTO userDTO = userServices.findById(userId);
        taskDTO.setUserId(userId);
        try {
            TaskDTO newTask = taskServices.createNewTask(taskDTO, userDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Задача '" + taskDTO.getNameTask() + "' успешно создана!");

            // Flash-атрибут будет доступен только в следующем запросе после редиректа
            redirectAttributes.addFlashAttribute("successMessage", "Задача '" + taskDTO.getNameTask() + "' успешно добавлена!");
            return "redirect:/tasks/" + newTask.getId();
        } catch (UserNotFound e) {
            throw new RuntimeException(e);
        }


    }
    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable Long id)  {
        log.debug("Запрос деталей задачи");
        TaskDTO taskDTO = taskServices.findById(id);
        Long userId = taskDTO.getUserId();
        ModelAndView mav = new ModelAndView("taskDetailsById");

        if(taskDTO != null){
            mav.addObject("task",taskDTO);
        }
        mav.addObject("userId", userId);
        return mav;
    }
    @GetMapping("/{id}/doneTaskId")
    public String doneTaskById(@PathVariable Long id, RedirectAttributes redirectAttributes){
        log.debug("Задача сделана");
        if(taskServices.isDoneTask(id)) throw new TaskAlreadyDone(String.format("Задача с таким ID %s уже выполнена ", String.valueOf(id)));
        else{
            taskServices.doneTask(id);
            redirectAttributes.addFlashAttribute("successMessage", "Задача '" + taskServices.findById(id).getNameTask() + "' успешно сделана!");
            return "redirect:/tasks/" + id;
        }

    }

    @GetMapping("/{id}/updateDedlain")
    public ModelAndView showUpdateDedlain(@PathVariable Long id,@ModelAttribute String newDate ){
        log.debug("Обновить дедлайн");
        TaskDTO taskDTO = taskServices.findById(id);
        ModelAndView mav = new ModelAndView("updateDedlain");
        if(taskDTO != null){
            mav.addObject("task",taskDTO);
        }
        return mav;

    }
    @PostMapping("/{id}/updateDedlain")
    public String updateDeadline(@PathVariable Long id,
    @RequestParam String newDate,RedirectAttributes redirectAttributes){
        log.debug("Обновить дедлайн для задачи ID: {}", id);
        taskServices.upDateDeadline(id,newDate);
        TaskDTO updatedTask = taskServices.findById(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "У задачи '" + updatedTask.getNameTask() + "' установлен новый дедлайн: " + newDate);
        return "redirect:/tasks/" + id;
    }
    @GetMapping("/{id}/setStatus")
    public ModelAndView showUpdateStatus(@PathVariable Long id,@ModelAttribute String newStatus, RedirectAttributes redirectAttributes) {
        log.debug("Установить статус");
        TaskDTO taskDTO = taskServices.findById(id);
        ModelAndView mav = new ModelAndView("taskSetStatus");
        if (taskDTO != null){
            mav.addObject("task",taskDTO);
        }
        return mav;
    }
    @PostMapping("/{id}/setStatus")
    public String updateStatus(@PathVariable Long id,
                                 @RequestParam String newStatus,RedirectAttributes redirectAttributes){
        log.debug("Обновить status для задачи ID: {}", id);
        taskServices.setStatus(newStatus,id);
        TaskDTO updatedTask = taskServices.findById(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "У задачи '" + updatedTask.getNameTask() + "' установлен новый статус: " + newStatus);

        return "redirect:/tasks/" + id;
    }
    @GetMapping("/schedule")
    public ModelAndView schedule(){

        LinkedHashMap<String, TaskDTO> taskDTOS = taskServices.scheduleReminders();
        ModelAndView mav = new ModelAndView("schedule");
        mav.addObject("tasks",taskDTOS);


        return mav;
    }
}
