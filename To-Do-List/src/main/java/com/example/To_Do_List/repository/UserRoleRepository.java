package com.example.To_Do_List.repository;

import com.example.To_Do_List.model.Role;
import com.example.To_Do_List.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByName(UserRoles role);
}
