package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
