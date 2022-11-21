package com.uber.driver.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties("mysql")
@EnableTransactionManagement
@Getter
@Slf4j
public class MysqlConfig {
    private final Map<String, String> demoConfig = new HashMap<>();

    @Bean
    public DataSource getDataSource() {return MysqlConfig.getDataSource(demoConfig);}

    private static DataSource getDataSource(Map<String, String> demoConfig) {
        log.info("demoMysql config -- {}",demoConfig);
        HikariConfig hikariConfig = new ModelMapper().map(demoConfig,HikariConfig.class);
        return new HikariDataSource(hikariConfig);
    }
}
