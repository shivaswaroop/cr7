package com.uber.driver.pojos.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverRequest {

    @JsonProperty("name")
    @NotBlank(message = "Name can't be blank")
    private String name;

    @JsonProperty("mobile_no")
    @NotBlank(message = "Mobile number cannot be blank")
    private String phoneNo;

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    private String emial;

    @JsonProperty("address")
    @NotBlank(message="address can't be blank")
    private String address;

}
