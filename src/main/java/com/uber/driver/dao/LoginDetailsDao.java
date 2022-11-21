package com.uber.driver.dao;

import com.uber.driver.pojos.DTO.UserLoginDto;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class LoginDetailsDao extends MysqlBaseDao<Long, UserLoginDto> {
    protected LoginDetailsDao(DataSource datasource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, BaseRowMapper<UserLoginDto> baseRowMapper) {
        super(datasource, jdbcTemplate, namedParameterJdbcTemplate, baseRowMapper);
    }
    private final String INSERT_QUERY ="insert into login_details (driver_id,email,phone_no,passwords,deleted) values (?,?,?,?,?)";

    private final String SELECT_BY_USERNAME = "select * from login_details where (email=? or phone_no=?) and deleted <> 1";

    private final String SELECT_BY_DRIVERID = "select * from login_details where driver_id=? and deleted <> 1";

    private final String UPDATE_QUERY = "update login_details set email=?,phone_no=?, password=?,deleted=? where driver_id=?";
    @Override
    protected PreparedStatementCreator getInsertQueryStatementCreator(UserLoginDto value) {
        return Connection -> {
            PreparedStatement ps = Connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            int i=1;
            ps.setString(i++,value.getDriverId());
            ps.setString(i++, value.getEmail());
            ps.setString(i++, value.getPhoneno());
            ps.setString(i++, value.getPassword());
            ps.setBoolean(i++,false);
            return ps;
        };
    }

    @Override
    protected String findByIdQuery() {
        return null;
    }

    @Override
    protected String getUpdateQuery() {
        return null;
    }

    protected String getSelectQuery() {
        return SELECT_BY_USERNAME;
    }
    @Override
    protected PreparedStatementCallback<Integer> getUpdateQueryStatementCallBack(UserLoginDto value) {
        return connection ->{
            PreparedStatement ps = connection.getConnection().prepareStatement(UPDATE_QUERY,Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setString(i++, value.getEmail());
            ps.setString(i++, value.getPhoneno());
            ps.setString(i++, value.getPassword());
            ps.setBoolean(i++,value.isDeleted());
            ps.setString(i++,value.getDriverId());
            return ps.executeUpdate();
        };
    }


    public UserLoginDto findByUsernamePhone(String userName, String phone) {
        try {
            return jdbcTemplate.queryForObject(getSelectQuery(),baseRowMapper, new Object[]{userName,phone});
        } catch(DataAccessException e){
            return null;
        }
    }

    public UserLoginDto findByDriverId(String driverId){
        try{
            return jdbcTemplate.queryForObject(SELECT_BY_DRIVERID,baseRowMapper,new Object[]{driverId});
        }catch(DataAccessException e) {
            return null;
        }
    }
}
