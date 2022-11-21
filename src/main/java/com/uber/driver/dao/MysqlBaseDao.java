package com.uber.driver.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.io.Serializable;

public abstract class MysqlBaseDao <K,V extends Serializable> {

    protected final DataSource datasource;

    protected final JdbcTemplate jdbcTemplate;

    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected final BaseRowMapper<V> baseRowMapper;

    protected MysqlBaseDao(DataSource datasource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, BaseRowMapper<V> baseRowMapper) {
        this.datasource = datasource;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.baseRowMapper = baseRowMapper;
    }

    protected abstract PreparedStatementCreator getInsertQueryStatementCreator(V value);

    protected abstract String findByIdQuery();

    protected abstract String getUpdateQuery();

    protected abstract PreparedStatementCallback<Integer> getUpdateQueryStatementCallBack(V value);

    public Long save(V value) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try{
            jdbcTemplate.update(getInsertQueryStatementCreator(value),keyHolder);
            return keyHolder.getKey().longValue();
        } catch (DataAccessException e) {
            throw new DatabaseFailureException("error occured while saving",500);
        }
    }

    public Integer update(V value) {
        try{
            return jdbcTemplate.execute(getUpdateQuery(),getUpdateQueryStatementCallBack(value));
        } catch (DataAccessException e) {
            throw new DatabaseFailureException("error occured while saving",500);
        }
    }
}
