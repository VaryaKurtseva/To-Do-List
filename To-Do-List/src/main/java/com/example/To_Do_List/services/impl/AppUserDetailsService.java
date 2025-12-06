package com.example.To_Do_List.services.impl;

import com.example.To_Do_List.Errors.UserNotFound;
import com.example.To_Do_List.model.User;
import com.example.To_Do_List.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

public class AppUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // Ищем пользователя в БД
        User user = userRepository.findByUsername(username);
        if(user == null) try {
            throw new UserNotFound( "Пользователь " + username + " не найден!");
        } catch (UserNotFound e) {
            throw new RuntimeException(e);
        }


        // Конвертируем в Spring Security User
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                // Конвертируем роли в GrantedAuthority
                user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                        .collect(Collectors.toList())
        );
    }

}
