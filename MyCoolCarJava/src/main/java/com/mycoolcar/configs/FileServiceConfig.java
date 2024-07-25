package com.mycoolcar.configs;

import com.mycoolcar.services.AwsS3ServiceImpl;
import com.mycoolcar.services.FileService;
import com.mycoolcar.services.GoogleFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FileServiceConfig {

    @Value("${file.service.provider}")
    private String fileServiceProvider;

    @Bean
    public FileService fileService(AwsS3ServiceImpl awsS3ServiceImpl, GoogleFileServiceImpl googleFileServiceImpl) {
        log.info("Configuring FileService with provider: {}", fileServiceProvider);
        if ("aws".equalsIgnoreCase(fileServiceProvider)) {
            log.info("Using AWS S3 File Service");
            return awsS3ServiceImpl;
        } else if ("gcp".equalsIgnoreCase(fileServiceProvider)) {
            log.info("Using Google Cloud File Service");
            return googleFileServiceImpl;
        } else {
            log.error("Invalid file service provider: {}", fileServiceProvider);
            throw new IllegalArgumentException("Invalid file service provider: " + fileServiceProvider);
        }
    }
}
