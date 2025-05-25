package com.example.schedule.service;

import com.example.schedule.dto.TodoDeleteRequestDto;
import com.example.schedule.dto.TodoResponseDto;
import com.example.schedule.dto.TodoUpdateRequestDto;
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

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository repository, TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
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

        todoRepository.save(todo);
    }

    public List<TodoResponseDto> getTodos(String startDate, String endDate, String writer) {
        List<TodoResponseDto> todoResponseDtos = todoRepository.findTodos(startDate, endDate, writer);

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
        TodoResponseDto todoResponseDto = todoRepository.findResponseDtoById(id)
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

    public void updateTodo(Long todoId, TodoUpdateRequestDto dto) {
        Todo todo = todoRepository.findById(todoId);
        if (todo == null) {
            throw new IllegalArgumentException("일정이 존재하지 않습니다.");
        }

        User user = userRepository.findById(todo.getUserId());
        if (user == null || !user.getPassword().equals(dto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        todo.setTitle(dto.getTitle());
        todo.setContent(dto.getContent());
        todo.setModifiedAt(LocalDateTime.now());
        todoRepository.update(todo);

        user.setName(dto.getName());
        userRepository.update(user);
    }

    public boolean deleteTodo(Long todoId, TodoDeleteRequestDto dto) {
        Todo todo = todoRepository.findById(todoId);
        if (todo == null) {
            return false;
        }

        User user = userRepository.findById(todo.getUserId());
        if (user == null) {
            return false;
        }

        // 이름과 비밀번호가 일치하는지 확인
        if (!user.getName().equals(dto.getName()) || !user.getPassword().equals(dto.getPassword())) {
            return false;
        }

        todoRepository.deleteById(todoId);
        return true;
    }



}
