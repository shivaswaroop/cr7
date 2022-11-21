package com.uber.driver.dao;


import com.uber.driver.pojos.DTO.DriverDto;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class DriverDao extends MysqlBaseDao<Long, DriverDto> {
    protected DriverDao(DataSource datasource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, BaseRowMapper<DriverDto> baseRowMapper) {
        super(datasource, jdbcTemplate, namedParameterJdbcTemplate, baseRowMapper);
    }

    private String SELECT_ALL = "select * from driver where deleted <>1";

    private String INSERT_QUERY = "Insert into driver (`driver_id`,`name`,`phone_no`,`email`,`address`,`deleted`) values (?,?,?,?,?,?);";

    private String UPDATE_QUERY = "update driver set name=?,phone_no=?,email=?,address=? deleted = ? where driver_id =?";

    private String SELECT_BY_ID = "select * from driver wher driver_id=? and deleted<>1";

    private String SELECT_BY_NAME_PHONE_EMAIL = "selct * from driver where name=? or emial=? or phone_no = ? and deleted<>1";

    protected String getSELECT_ALL(){
        return SELECT_ALL;
    }

    @Override
    protected PreparedStatementCreator getInsertQueryStatementCreator(DriverDto value) {
        return connection ->{
            PreparedStatement ps =  connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            int i=1;
            ps.setString(i++,value.getName());
            ps.setString(i++, value.getPhoneNo());
            ps.setString(i++, value.getEmail());
            ps.setString(i++, value.getAddress());
            ps.setBoolean(i++, value.getDeleted());
            return ps;

        };
    }

    @Override
    protected String findByIdQuery() {
        return SELECT_BY_ID;
    }

    @Override
    protected String getUpdateQuery() {
        return null;
    }

    protected String getSelectByPhoneEmail(){
        return SELECT_BY_NAME_PHONE_EMAIL;
    }
    @Override
    protected PreparedStatementCallback<Integer> getUpdateQueryStatementCallBack(DriverDto value) {
        return connection ->{
            PreparedStatement ps = connection.getConnection().prepareStatement(UPDATE_QUERY,Statement.RETURN_GENERATED_KEYS);
            int i=1;
            ps.setString(i++, value.getName());
            ps.setString(i++, value.getPhoneNo());
            ps.setString(i++, value.getEmail());
            ps.setString(i++, value.getAddress());
            ps.setBoolean(i++,value.getDeleted());
            ps.setString(i++, value.getDriverId());
            return ps.executeUpdate();
        };
    }


    public DriverDto getDriverById(String driverId) {
        try{
            return jdbcTemplate.queryForObject(findByIdQuery(),baseRowMapper,new Object[]{driverId});
        } catch(DataAccessException e){
            return null;
        }
    }


    public DriverDto getDriverByPhoeNameEmail(String name,String phone,String email) {
        try{
            return jdbcTemplate.queryForObject(getSelectByPhoneEmail(),baseRowMapper,new Object[]{name, email, phone});
        } catch(DataAccessException e){
            return null;
        }
    }
}
