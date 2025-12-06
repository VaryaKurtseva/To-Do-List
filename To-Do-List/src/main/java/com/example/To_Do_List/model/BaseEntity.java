package com.example.To_Do_List.model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    public long getId() {return id;}
    protected void setId(long id) {this.id = id;}
}
