package com.example.schedule.repository;

import com.example.schedule.dto.TodoResponseDto;
import com.example.schedule.entity.Todo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

    public Optional<TodoResponseDto> findResponseDtoById(Long id) {
        String sql = "SELECT t.*, u.name FROM todo t JOIN user u ON t.user_id = u.id WHERE t.id = ?";
        List<TodoResponseDto> todoResponseDtos = jdbcTemplate.query(sql, todoRowMapper(), id);
        return todoResponseDtos.isEmpty() ? Optional.empty() : Optional.of(todoResponseDtos.get(0));
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

    public Todo findById(Long id) {
        String sql = "SELECT * FROM todo WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Todo.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void update(Todo todo) {
        String sql = "UPDATE todo SET title = ?, content = ?, modified_at = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                todo.getTitle(),
                todo.getContent(),
                todo.getModifiedAt(),
                todo.getId()
        );
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM todo WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
