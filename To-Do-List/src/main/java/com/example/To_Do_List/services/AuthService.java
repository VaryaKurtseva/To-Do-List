package com.example.To_Do_List.services;

import com.example.To_Do_List.DTO.UserRegistrationDto;
import com.example.To_Do_List.model.User;

public interface AuthService {
    void register(UserRegistrationDto registrationDTO);
    User getUserByUsername(String username);

}
