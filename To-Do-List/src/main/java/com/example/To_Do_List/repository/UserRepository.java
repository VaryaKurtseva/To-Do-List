package com.example.To_Do_List.repository;

import com.example.To_Do_List.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    @Query("SELECT u FROM User u JOIN FETCH u.tasks WHERE u.username = :username and u.surname = :surname")
    User findByUsernameAndSurname(@Param("username") String username, @Param("surname") String surname);


    Page<User> findByUsernameContainingIgnoreCaseOrSurnameContainingIgnoreCase(
            String username,String surname, Pageable pageable);


    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    User findByUsername(@Param("username") String username);
}
// @Modifying — помечает запросы UPDATE/DELETE + транзакцию добавить