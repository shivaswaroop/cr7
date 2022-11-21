package com.uber.driver.dao.mapper;

import com.uber.driver.dao.BaseRowMapper;
import com.uber.driver.pojos.DTO.UserLoginDto;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserLoginMapper implements BaseRowMapper<UserLoginDto> {
    @Override
    public UserLoginDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserLoginDto.builder()
                .phoneno(rs.getString("phone_no"))
                .driverId(rs.getString("driver_id"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .deleted(rs.getBoolean("deleted"))
                .created(rs.getTimestamp("created"))
                .updated(rs.getTimestamp("updated"))
                .build();
    }
}
