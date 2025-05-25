package com.example.schedule.repository;

import com.example.schedule.domain.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TodoRepository {

    private final JdbcTemplate jdbcTemplate;

    public TodoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Todo todo) {
        String sql = "INSERT INTO todo (title, content, writer, password, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                todo.getTitle(),
                todo.getContent(),
                todo.getWriter(),
                todo.getPassword(),
                todo.getCreatedAt(),
                todo.getModifiedAt());
    }
}
