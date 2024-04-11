package org.bcb.dao;

import org.bcb.exception.DaoException;
import org.bcb.model.Tag;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcTagDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTagDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Tag getTagById(int id) {
        Tag tag = null;
        String sql = "SELECT * FROM tag WHERE patient_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            tag = mapToTag(rowSet);
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return tag;
    }
    public List<Tag> searchForTagByName(String tagName, boolean isStrict) {
        List<Tag> results = new ArrayList<>();
        String sql = "SELECT * FROM tag WHERE name ILIKE ?";
        if (!isStrict) {
            tagName = "%" + tagName + "%";
        }
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, tagName);
            while (rowSet.next()) {
                results.add(mapToTag(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return results;
    }


    public Tag mapToTag(SqlRowSet rowSet) {
        Tag tag = new Tag(
                rowSet.getInt("tag_id"),
                rowSet.getString("name"),
                rowSet.getBoolean("isDiagnosis")
        );
        return tag;
    }
}
