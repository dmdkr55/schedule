package com.example.schedule.service;

import com.example.schedule.dto.TodoResponseDto;
import com.example.schedule.entity.Todo;
import com.example.schedule.dto.TodoRequestDto;
import com.example.schedule.entity.User;
import com.example.schedule.repository.TodoRepository;
import com.example.schedule.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final TodoRepository repository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void createTodo(TodoRequestDto dto) {
        // 1. 작성자 정보로 사용자 조회
        User user = userRepository.findByNameAndPassword(dto.getWriter(), dto.getPassword());

        // 2. 없으면 새 사용자 등록
        if (user == null) {
            user = new User(dto.getWriter(), dto.getPassword());
            Long userId = userRepository.save(user);
            user.setId(userId);
        }

        // 3. 일정 저장
        Todo todo = new Todo();
        todo.setTitle(dto.getTitle());
        todo.setContent(dto.getContent());
        todo.setUserId(dto.getUserId());
        todo.setCreatedAt(LocalDateTime.now());
        todo.setModifiedAt(LocalDateTime.now());
        todo.setUserId(user.getId());

        repository.save(todo);
    }

    public List<TodoResponseDto> getTodos(String startDate, String endDate, String writer) {
        List<TodoResponseDto> todoResponseDtos = repository.findTodos(startDate, endDate, writer);

        return todoResponseDtos.stream()
                .map(todoResponseDto -> new TodoResponseDto(
                        todoResponseDto.getId(),
                        todoResponseDto.getTitle(),
                        todoResponseDto.getContent(),
                        todoResponseDto.getCreatedAt(),
                        todoResponseDto.getModifiedAt(),
                        todoResponseDto.getUserId(),
                        todoResponseDto.getWriter()))
                .collect(Collectors.toList());
    }

    public TodoResponseDto getTodoById(Long id) {
        TodoResponseDto todoResponseDto = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 일정이 존재하지 않습니다."));

        return new TodoResponseDto(
                todoResponseDto.getId(),
                todoResponseDto.getTitle(),
                todoResponseDto.getContent(),
                todoResponseDto.getCreatedAt(),
                todoResponseDto.getModifiedAt(),
                todoResponseDto.getUserId(),
                todoResponseDto.getWriter());
    }

}
