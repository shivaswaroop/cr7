package com.uber.driver.dao.mapper;

import com.uber.driver.dao.BaseRowMapper;
import com.uber.driver.pojos.DTO.DriverDto;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DriverMapper implements BaseRowMapper<DriverDto> {
    @Override
    public DriverDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DriverDto.builder()
                .id(rs.getLong("id"))
                .driverId(rs.getString("driver_id"))
                .name(rs.getString("name"))
                .phoneNo(rs.getString("phone_no"))
                .address(rs.getString("address"))
                .email(rs.getString("email"))
                .deleted(rs.getBoolean("isdeleted"))
                .created(rs.getTimestamp("created"))
                .updated(rs.getTimestamp("updated"))
                .build();
    }
}
