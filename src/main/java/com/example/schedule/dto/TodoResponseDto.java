package com.example.schedule.dto;

import java.time.LocalDateTime;

public class TodoResponseDto {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
