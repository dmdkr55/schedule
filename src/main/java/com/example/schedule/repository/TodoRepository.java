package com.example.schedule.repository;

import com.example.schedule.dto.TodoResponseDto;
import com.example.schedule.entity.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TodoRepository {

    private final JdbcTemplate jdbcTemplate;

    public TodoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Todo todo) {
        String sql = "INSERT INTO todo (title, content, created_at, modified_at, user_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                todo.getTitle(),
                todo.getContent(),
                todo.getCreatedAt(),
                todo.getModifiedAt(),
                todo.getUserId());
    }

    public List<TodoResponseDto> findTodos(String startDate, String endDate, String writer) {
        StringBuilder sql = new StringBuilder("SELECT t.*, u.name FROM todo t JOIN user u ON t.user_id = u.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND DATE(t.modified_at) >= ?");
            params.add(LocalDate.parse(startDate));
        }

        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND DATE(t.modified_at) <= ?");
            params.add(LocalDate.parse(endDate));
        }

        if (writer != null && !writer.isEmpty()) {
            sql.append(" AND u.name = ?");
            params.add(writer);
        }

        sql.append(" ORDER BY t.modified_at DESC");

        return jdbcTemplate.query(sql.toString(), todoRowMapper(), params.toArray());
    }

    private RowMapper<TodoResponseDto> todoRowMapper() {
        return (rs, rowNum) -> {
            TodoResponseDto todoResponseDto = new TodoResponseDto();
            todoResponseDto.setId(rs.getLong("id"));
            todoResponseDto.setTitle(rs.getString("title"));
            todoResponseDto.setContent(rs.getString("content"));
            todoResponseDto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            todoResponseDto.setModifiedAt(rs.getTimestamp("modified_at").toLocalDateTime());
            todoResponseDto.setUserId(rs.getLong("user_id"));
            todoResponseDto.setWriter(rs.getString("name"));
            return todoResponseDto;
        };
    }

    public Optional<TodoResponseDto> findById(Long id) {
        String sql = "SELECT t.*, u.name FROM todo t JOIN user u ON t.user_id = u.id WHERE t.id = ?";
        List<TodoResponseDto> todos = jdbcTemplate.query(sql, todoRowMapper(), id);
        return todos.isEmpty() ? Optional.empty() : Optional.of(todos.get(0));
    }


}
