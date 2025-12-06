package com.example.To_Do_List.model;

import jakarta.persistence.*;



@Entity
@Table(name = "roles")
public class Role extends BaseEntity  {
    @Enumerated(EnumType.STRING) // сохраняем enaum в бд в виде строк
    @Column(unique = true)
    private UserRoles name;
    public Role(){}

    public Role(UserRoles name) {
        this.name = name;
    }

    public UserRoles getName() {
        return name;
    }

    public void setName(UserRoles name) {
        this.name = name;
    }
}
