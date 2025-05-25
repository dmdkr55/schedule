package com.example.schedule.repository;

import com.example.schedule.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByNameAndPassword(String name, String password) {
        String sql = "SELECT * FROM user WHERE name = ? AND password = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper(), name, password);
        return users.isEmpty() ? null : users.get(0);
    }

    public Long save(User user) {
        String sql = "INSERT INTO user (name, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getPassword());

        // 마지막으로 삽입된 ID 조회
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper(), id);
        return users.isEmpty() ? null : users.get(0);
    }

    private RowMapper<User> userRowMapper() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("password")
                );
            }
        };
    }

}
