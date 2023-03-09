package com.example.wordle.config;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
@Builder
public class Properties {

    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.driverClassName}")
    private String DRIVER;

    @Value("${spring.datasource.password}")
    private String PASS;

    @Value("${spring.datasource.username}")
    private String USER;


}
