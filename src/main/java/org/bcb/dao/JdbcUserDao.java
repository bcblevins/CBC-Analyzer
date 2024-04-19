package org.bcb.dao;

import org.bcb.exception.DaoException;
import org.bcb.model.Patient;
import org.bcb.model.User;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;

public class JdbcUserDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User getUserByUsernameAndPassword(String userName, String password) {
        User user = null;
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userName, password);
            if (rowSet.next()) {
                user = mapToUser(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return user;
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
