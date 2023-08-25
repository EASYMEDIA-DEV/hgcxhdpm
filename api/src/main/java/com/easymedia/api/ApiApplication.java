package com.easymedia.api;

import com.easymedia.property.FileUploadProperties;
import com.easymedia.property.JwtProperties;
import com.easymedia.property.UploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = {"com.easymedia"})
@EnableConfigurationProperties({JwtProperties.class, UploadProperties.class, FileUploadProperties.class})
@EnableCaching
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
