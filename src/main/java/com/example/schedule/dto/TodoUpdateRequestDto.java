package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class TodoUpdateRequestDto {
    private String title;
    private String content;
    private String name;
    private String password;
}
