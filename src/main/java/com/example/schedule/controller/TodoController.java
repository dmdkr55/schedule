package com.example.schedule.controller;

import com.example.schedule.dto.TodoRequestDto;
import com.example.schedule.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createTodo(@RequestBody TodoRequestDto dto) {
        service.createTodo(dto);
        return ResponseEntity.ok("일정이 등록되었습니다.");
    }
}
