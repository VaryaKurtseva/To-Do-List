package com.example.To_Do_List.services;

import com.example.To_Do_List.DTO.TaskDTO;
import com.example.To_Do_List.DTO.UserDTO;
import com.example.To_Do_List.Errors.UserNotFound;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;


public interface UserServices {
    List<UserDTO> findAll();
    UserDTO findById(Long id) throws UserNotFound;

    void createNewUser(UserDTO user);
    int increamentProductivity(Long id) throws UserNotFound;
    List<TaskDTO> getUserTask(Long id) throws UserNotFound;


    Page<UserDTO> allUsersPaginated(Pageable pageable);

    Page<UserDTO> searchUsers(String search,Pageable pageable);


    void deleteById(long id) throws UserNotFound;
}
