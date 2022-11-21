package com.uber.driver.pojos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DriverSignUpRequest {

    @JsonProperty("driver_request")
    private DriverRequest driverRequest;

    @JsonProperty("password")
    @NotBlank(message = "password can't be null or blank")
    private String password;
}
