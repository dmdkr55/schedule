package com.example.schedule.controller;

import com.example.schedule.dto.TodoDeleteRequestDto;
import com.example.schedule.dto.TodoRequestDto;
import com.example.schedule.dto.TodoResponseDto;
import com.example.schedule.dto.TodoUpdateRequestDto;
import com.example.schedule.service.TodoService;
import org.springframework.http.HttpStatus;
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

    //일정 생성
    @PostMapping
    public ResponseEntity<String> createTodo(@RequestBody TodoRequestDto dto) {
        service.createTodo(dto);
        return ResponseEntity.ok("일정이 등록되었습니다.");
    }

    //전체 일정 조회
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getTodos(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String writer) {
        List<TodoResponseDto> todos = service.getTodos(startDate, endDate, writer);
        return ResponseEntity.ok(todos);
    }

    //선택 일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id) {
        TodoResponseDto dto = service.getTodoById(id);
        return ResponseEntity.ok(dto);
    }

    //일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTodo(@PathVariable Long id, @RequestBody TodoUpdateRequestDto dto) {
        try {
            service.updateTodo(id, dto);
            return ResponseEntity.ok("일정이 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id, @RequestBody TodoDeleteRequestDto dto) {
        boolean deleted = service.deleteTodo(id, dto);
        if (deleted) {
            return ResponseEntity.ok("일정이 성공적으로 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호가 일치하지 않거나 일정이 존재하지 않습니다.");
        }
    }

}
