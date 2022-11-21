package com.uber.driver.pojos.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DriverDto implements Serializable {

    private long id;
    private String driverId;
    private String name;
    private String phoneNo;
    private String email;
    private String address;
    private Boolean deleted;
    private Timestamp created;
    private Timestamp updated;

}
