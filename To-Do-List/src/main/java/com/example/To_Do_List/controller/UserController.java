package com.example.To_Do_List.controller;

import com.example.To_Do_List.DTO.TaskDTO;
import com.example.To_Do_List.DTO.UserDTO;
import com.example.To_Do_List.DTO.UserProfileView;
import com.example.To_Do_List.Errors.UserNotFound;
import com.example.To_Do_List.model.User;
import com.example.To_Do_List.services.AuthService;
import com.example.To_Do_List.services.UserServices;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.AuthProvider;
import java.security.Principal;
import java.util.List;
@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    public UserServices userServices;
    @Autowired
    public AuthService authService;
    @GetMapping("/all")
    public String getAllUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(required = false) String search,
            Model model){
        log.debug("Отображение списка всех пользователей: страница={}, размер={}, поиск={}",page,size,search);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        if (search != null && !search.trim().isEmpty()) {
            // Режим поиска
            model.addAttribute("allUsers", userServices.searchUsers(search,pageable));
            model.addAttribute("search", search);
        } else {
            // Режим пагинации

            Page<UserDTO> userDTOPage = userServices.allUsersPaginated(pageable);
            model.addAttribute("allUsers", userDTOPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userDTOPage.getTotalPages());
            model.addAttribute("totalItems", userDTOPage.getTotalElements());
            model.addAttribute("sortBy",sortBy);
            model.addAttribute("search", search);
        }
        return "usersAll";
    }
    @GetMapping("/{id}/userDelete")
    public String deleteByNameAndSurname(@PathVariable("id") Long id,

                                         RedirectAttributes redirectAttributes){
        log.debug("Запрос на удаления пользователя");
        userServices.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь " + id + " удален!");
        return "redirect:/users/all";

    }



    @GetMapping("/new")
    public String showUserForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "userForm";
    }

    @PostMapping
    public String addUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Обработка POST запроса на добавление пользователя");
        if(bindingResult.hasErrors()) return "userForm";
        userServices.createNewUser(userDTO);

        // Flash-атрибут будет доступен только в следующем запросе после редиректа
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь '" + userDTO.getUsername() + "' успешно добавлен!");
        return "redirect:/users/all";
    }
    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        String username = principal.getName();
        log.debug("Отображение профиля пользователя: {}", username);

        User user = authService.getUserByUsername(username);

        UserProfileView userProfileView = new UserProfileView(
                username,
                user.getEmail(),
                user.getSurname(),
                user.getDateOfBirthday()

        );

        model.addAttribute("user", userProfileView);
        model.addAttribute("id", user.getId());

        return "profile";
    }
    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable Long id){
        log.debug("Запрос деталей пользователя");
        UserDTO user = userServices.findById(id);
        ModelAndView mav = new ModelAndView("userDetailsById");
        if(user != null){
            mav.addObject("user",user);
        }
        return mav;
    }
    @GetMapping("/{id}/userTasks")
    public ModelAndView getUserTask(@PathVariable Long id) {
        log.debug("Запрос на просмотр задачек пользователя");
        UserDTO user = userServices.findById(id);
        ModelAndView mav = new ModelAndView("userGetTask");
        if(user != null){
            List<TaskDTO> task = userServices.getUserTask(id);
            mav.addObject("user",user);
            mav.addObject("tasks",task);
        }
        return mav;
    }
}
