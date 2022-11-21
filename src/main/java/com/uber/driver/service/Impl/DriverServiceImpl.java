package com.uber.driver.service.Impl;

import com.uber.driver.constants.DriverConstants;
import com.uber.driver.dao.DriverDao;
import com.uber.driver.dao.LoginDetailsDao;
import com.uber.driver.exceptions.InternalServerException;
import com.uber.driver.pojos.DTO.DriverDto;
import com.uber.driver.pojos.DTO.UserLoginDto;
import com.uber.driver.pojos.requests.DriverRequest;
import com.uber.driver.pojos.requests.DriverSignUpRequest;
import com.uber.driver.pojos.requests.UserLoginUpdateRequest;
import com.uber.driver.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.BadRequestException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {

    @Autowired
    DriverDao driverDao;

    @Autowired
    LoginDetailsDao loginDetailsDao;

    private static final String DELIMITER = "-";
    private static final String PREFIX = "DVR";
    private static final String THREE_DIGITS_FORMAT = "%03d";
    private static final String TWO_DIGITS_FORMAT = "%02d";
    private static final int RANDOM_ALPHABETS_LENGTH = 2;
    private static final int RANDOM_NUMERIC_LENGTH = 2;

    private SecretKeySpec secretKey;

    private IvParameterSpec ivspec;

    @Override
    public void signUpNewDriver(DriverSignUpRequest driverRequest) {
        String driverId = generateDriverId();
        DriverDto driver = driverDao.getDriverByPhoeNameEmail(driverRequest.getDriverRequest().getName()
                                                    ,driverRequest.getDriverRequest().getPhoneNo()
                                                    ,driverRequest.getDriverRequest().getEmial());
        if(ObjectUtils.isNotEmpty(driver)){
            throw new BadRequestException("Driver already exist please try login!");
        }
        DriverDto driverDto = DriverDto.builder()
                .driverId(driverId)
                .name(driverRequest.getDriverRequest().getName())
                .email(driverRequest.getDriverRequest().getEmial())
                .phoneNo(encrypt(driverRequest.getDriverRequest().getPhoneNo()))
                .address(driverRequest.getDriverRequest().getAddress())
                .deleted(false)
                .build();

        UserLoginDto userLoginDto = UserLoginDto.builder()
                                    .driverId(driverId).phoneno(encrypt(driverRequest.getDriverRequest()
                                    .getPhoneNo()))
                                    .email(driverRequest.getDriverRequest().getEmial())
                                    .password(encrypt(driverRequest.getPassword()))
                                    .deleted(false)
                                    .build();
        loginDetailsDao.save(userLoginDto);
        driverDao.save(driverDto);
    }

    @Override
    public void updateDriverDetails(DriverRequest driverRequest) {
        DriverDto driver = driverDao.getDriverByPhoeNameEmail(driverRequest.getName(),driverRequest.getPhoneNo(),driverRequest.getEmial());
        if(ObjectUtils.isEmpty(driver)){
            throw new BadRequestException("Driver doesn't exist please try signup!");
        }
        DriverDto driverDto = DriverDto.builder()
                .driverId(driver.getDriverId())
                .name(driverRequest.getName())
                .email(driverRequest.getEmial())
                .phoneNo(encrypt(driverRequest.getPhoneNo()))
                .address(driverRequest.getAddress())
                .deleted(false)
                .build();
        driverDao.update(driverDto);
        UserLoginDto user = loginDetailsDao.findByDriverId(driver.getDriverId());
        if (driver == null){
            UserLoginDto userLoginDto = UserLoginDto.builder()
                    .driverId(driver.getDriverId())
                    .phoneno(encrypt(driverRequest.getPhoneNo()))
                    .email(driverRequest.getEmial())
                    .password(encrypt("driverRequest.getPassword()"))
                    .build();
            loginDetailsDao.save(userLoginDto);
        } else {
            UserLoginDto userLoginDto = UserLoginDto.builder()
                    .driverId(user.getDriverId())
                    .email(driverRequest.getEmial())
                    .phoneno(driverRequest.getPhoneNo())
                    .password(user.getPassword())
                    .deleted(false)
                    .build();
            loginDetailsDao.update(userLoginDto);
        }
    }

    @Override
    public List<DriverDto> getAllDrivers() {
        return null;
    }

    @Override
    public DriverDto findDriverByDriverId(String driverId) {
        return driverDao.getDriverById(driverId);
    }

    @Override
    public void updatePassword(UserLoginUpdateRequest userLoginUpdateRequest) {
        UserLoginDto user = loginDetailsDao.findByUsernamePhone(userLoginUpdateRequest.getEmail(),encrypt(userLoginUpdateRequest.getPhone()));
        if(user == null) {
            throw new BadRequestException("No driver found with the given details");
        }
        user.setPassword(encrypt(userLoginUpdateRequest.getPassword()));
        loginDetailsDao.update(user);
    }

    private String generateDriverId(){

        StringBuilder stringBuilder = new StringBuilder();
        DateTime dateTime = new DateTime();
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1
                .append(String.format(THREE_DIGITS_FORMAT, dateTime.millisOfSecond().get()))
                .append(String.format(TWO_DIGITS_FORMAT, dateTime.secondOfMinute().get()))
                .append(String.format(TWO_DIGITS_FORMAT, dateTime.minuteOfHour().get()))
                .append(String.format(TWO_DIGITS_FORMAT, dateTime.hourOfDay().get()))
                .append(String.format(TWO_DIGITS_FORMAT, dateTime.dayOfMonth().get()))
                .append(String.format(TWO_DIGITS_FORMAT, dateTime.monthOfYear().get()))
                .append(String.format(TWO_DIGITS_FORMAT, dateTime.yearOfCentury().get()));

        String randomAlphabets = RandomStringUtils.randomAlphabetic(RANDOM_ALPHABETS_LENGTH).toUpperCase();
        String randomNumeric = RandomStringUtils.randomNumeric(RANDOM_NUMERIC_LENGTH);

        return stringBuilder.append(PREFIX).append(DELIMITER)
                .append(stringBuilder1.toString()).append(DELIMITER)
                .append(randomAlphabets).append(randomNumeric)
                .toString();

    }


    private String encrypt(String value) {
        if (value != null) {
            try {
                Cipher cipher = getCipher();
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
                return Base64.encodeBase64String(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception ex) {
                log.error("Exception occurred while encrypting ", ex);
                throw new InternalServerException(DriverConstants.INTERNAL_SERVER_EXCEPTION,
                       500);
            }
        }
        return null;
    }

    private Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES/CBC/PKCS5Padding");
    }


    public String decrypt(String encrypted) {
        if (encrypted != null) {
            try {
                Cipher cipher = getCipher();
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
                byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
                return new String(original);
            } catch (Exception ex) {
                log.error("Exception occurred while decrypting ", ex);
                throw new InternalServerException(DriverConstants.INTERNAL_SERVER_EXCEPTION,
                        500);
            }
        }
        return null;
    }
}
