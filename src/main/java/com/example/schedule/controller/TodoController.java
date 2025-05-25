package com.example.schedule.controller;

import com.example.schedule.dto.TodoRequestDto;
import com.example.schedule.dto.TodoResponseDto;
import com.example.schedule.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getTodos(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String writer) {
        List<TodoResponseDto> todos = service.getTodos(startDate, endDate, writer);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id) {
        TodoResponseDto dto = service.getTodoById(id);
        return ResponseEntity.ok(dto);
    }

}
