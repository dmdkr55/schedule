package com.example.schedule.service;

import com.example.schedule.domain.Todo;
import com.example.schedule.dto.TodoRequestDto;
import com.example.schedule.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TodoService {

    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public void createTodo(TodoRequestDto dto) {
        Todo todo = new Todo();
        todo.setTitle(dto.getTitle());
        todo.setContent(dto.getContent());
        todo.setWriter(dto.getWriter());
        todo.setPassword(dto.getPassword());
        todo.setCreatedAt(LocalDateTime.now());
        todo.setModifiedAt(LocalDateTime.now());

        repository.save(todo);
    }

}
