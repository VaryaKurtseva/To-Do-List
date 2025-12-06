package com.example.To_Do_List.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(nullable = false, unique = true, name = "userName")
    private String username;
    @Column(name = "surname")
    private String surname;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(unique = true, nullable = false, name = "email")
    private String email;


    @Column(name = "dateOfBirthday")
    private String dateOfBirthday;
    @Column(name = "productivity")
    private int productivity = 0;
    @ManyToMany
    @JoinTable(
            name = "userWithTask",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();


    public User() {
    }



    public User(String username, String password, String email, String surname, String dateOfBirthday) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.setSurname(surname);
        this.setDateOfBirthday(dateOfBirthday);
    }


    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setId(Long id) {
        if(id > 0) this.id = id;
        else throw new RuntimeException("ID не может быть отрицательным");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;

    }

    public String getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(String dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;

    }

    public int incrementProductivity(){
        productivity += 5;
        return productivity;
    }

    public int getProductivity() {
        return productivity;
    }
}
