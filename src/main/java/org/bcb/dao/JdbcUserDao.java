package org.bcb.dao;

import org.bcb.exception.DaoException;
import org.bcb.model.LabTest;
import org.bcb.model.Patient;
import org.bcb.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User getUserByUsername(String userName) {
        User user = null;
        String sql = "SELECT * FROM \"user\" WHERE username = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userName);
            if (rowSet.next()) {
                user = mapToUser(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return user;
    }
    public User getUserById(int id) {
        User user = null;
        String sql = "SELECT * FROM \"user\" WHERE user_id = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if (rowSet.next()) {
                user = mapToUser(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return user;
    }
    public User createUser(User user) {
        User nUser = null;
        String sql = "INSERT INTO \"user\" (first_name, last_name, is_doctor, username, password) VALUES " +
                "(?,?,?,?,?) " +
                "RETURNING user_id;";
        try {
            int id = jdbcTemplate.queryForObject(sql, int.class, user.getFirstName(), user.getLastName(), user.isDoctor(), user.getUsername(), user.getPassword());
            nUser = getUserById(id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
        return nUser;
    }


    private User mapToUser(SqlRowSet rowSet) {
        User user = new User(
                rowSet.getInt("user_id"),
                rowSet.getString("first_name"),
                rowSet.getString("last_name"),
                rowSet.getBoolean("is_doctor"),
                rowSet.getString("username"),
                rowSet.getString("password")
        );
        return user;
    }
}
