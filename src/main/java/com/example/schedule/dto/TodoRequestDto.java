package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class TodoRequestDto {
    private String writer;
    private String password;
    private String title;
    private String content;
    private Long userId;
}
