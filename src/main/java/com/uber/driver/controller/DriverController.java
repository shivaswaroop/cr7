package com.uber.driver.controller;

import com.uber.driver.pojos.DTO.DriverDto;
import com.uber.driver.pojos.requests.DriverRequest;
import com.uber.driver.pojos.requests.DriverSignUpRequest;
import com.uber.driver.pojos.requests.UserLoginUpdateRequest;
import com.uber.driver.service.DriverService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    DriverService driverService;

    @PostMapping("driverSignUp")
    public ResponseEntity driverSignUp(@Valid @RequestBody DriverSignUpRequest driverSignUpRequest) {
        log.info("In API diverSignup");
        driverService.signUpNewDriver(driverSignUpRequest);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("updateDriverDetails")
    public ResponseEntity updateDriverDetails(@Valid @RequestBody DriverRequest driverRequest) {
        log.info("in Api updateDriverDetails");
        driverService.updateDriverDetails(driverRequest);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("getDriverDetails")
    public ResponseEntity getDriverDetails(@RequestParam("driver_id") String driverId) {
        log.info("In API getDriverDetails");
        DriverDto driver = driverService.findDriverByDriverId(driverId);
        return ResponseEntity.status(HttpStatus.OK).body(driver);
    }

    @PutMapping("updatePassword")
    public ResponseEntity updatePassword(@RequestBody UserLoginUpdateRequest userLoginUpdateRequest){
        driverService.getAllDrivers();
        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

}
