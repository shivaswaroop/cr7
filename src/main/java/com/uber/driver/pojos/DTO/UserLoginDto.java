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
public class UserLoginDto implements Serializable {

    private String driverId;
    private String email;
    private String phoneno;
    private String password;
    private boolean deleted;
    private Timestamp created;
    private Timestamp updated;
}
