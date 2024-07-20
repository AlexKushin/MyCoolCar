package com.mycoolcar.configs;

import com.mycoolcar.services.AwsS3ServiceImpl;
import com.mycoolcar.services.FileService;
import com.mycoolcar.services.GoogleFileServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileServiceConfig {

    @Value("${file.service.provider}")
    private String fileServiceProvider;

    @Bean
    public FileService fileService(AwsS3ServiceImpl awsS3ServiceImpl, GoogleFileServiceImpl googleFileServiceImpl) {
        if ("aws".equalsIgnoreCase(fileServiceProvider)) {
            return awsS3ServiceImpl;
        } else if ("gcp".equalsIgnoreCase(fileServiceProvider)) {
            return googleFileServiceImpl;
        } else {
            throw new IllegalArgumentException("Invalid file service provider: " + fileServiceProvider);
        }
    }
}
