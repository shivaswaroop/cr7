package com.uber.driver.service;

import com.uber.driver.pojos.DTO.DriverDto;
import com.uber.driver.pojos.requests.DriverRequest;
import com.uber.driver.pojos.requests.DriverSignUpRequest;
import com.uber.driver.pojos.requests.UserLoginUpdateRequest;

import java.util.List;

public interface DriverService {

    void signUpNewDriver(DriverSignUpRequest driverRequest);

    void updateDriverDetails(DriverRequest driverRequest);

    List<DriverDto> getAllDrivers();

    DriverDto findDriverByDriverId(String driverId);

    void updatePassword(UserLoginUpdateRequest userLoginUpdateRequest);

}
