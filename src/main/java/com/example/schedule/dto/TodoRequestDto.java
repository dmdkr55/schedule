package com.example.schedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TodoRequestDto {
    private String title;
    private String content;
    private String writer;
    private String password;
}
