package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class TodoResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long userId;
    private String writer;

    public TodoResponseDto() {
    }
}
