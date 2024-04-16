package org.bcb.dao;

import org.bcb.exception.DaoException;
import org.bcb.model.LabTest;
import org.bcb.model.Tag;
import org.springframework.dao.DataIntegrityViolationException;
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
        String sql = "SELECT * FROM tag WHERE tag_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if (rowSet.next()) {
                tag = mapToTag(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return tag;
    }

    public Tag searchForSingleTagByName(String tagName) {
        Tag result = null;
        String sql = "SELECT * FROM tag WHERE name = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, tagName);
            if (rowSet.next()) {
                result = mapToTag(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return result;
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

    public List<Tag> getTagsForTest(LabTest test) {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT tag.tag_id, tag.name, tag.is_diagnosis " +
                "FROM tag " +
                "JOIN test_tag ON test_tag.tag_id = tag.tag_id " +
                "WHERE test_tag.test_id = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, test.getId());
            while (rowSet.next()) {
                tags.add(mapToTag(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return tags;
    }

    public Tag createTag(Tag tag) {
        Tag nTag = null;
        String sql = "INSERT INTO tag (name, is_diagnosis) VALUES (?, ?) " +
                "RETURNING tag_id;";
        try {
            int id = jdbcTemplate.queryForObject(sql, int.class, tag.getName(), tag.isDiagnosis());
            nTag = getTagById(id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
        return nTag;

    }

    public Tag mapToTag(SqlRowSet rowSet) {
        Tag tag = new Tag(
                rowSet.getInt("tag_id"),
                rowSet.getString("name"),
                rowSet.getBoolean("is_diagnosis")
        );
        return tag;
    }
}
