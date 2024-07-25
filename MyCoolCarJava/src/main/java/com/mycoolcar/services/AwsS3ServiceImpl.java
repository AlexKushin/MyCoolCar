package com.mycoolcar.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.mycoolcar.entities.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class AwsS3ServiceImpl implements FileService {

    private final AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String bucketRegion;

    public AwsS3ServiceImpl(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public S3Object getFile(String keyName) {
        log.info("Fetching file with key: {}", keyName);
        S3Object s3Object = s3client.getObject(bucketName, keyName);
        log.info("File fetched successfully: {}", keyName);
        return s3Object;
    }

    @Override
    public List<String> listOfFiles() {
        log.info("Listing all files in bucket: {}", bucketName);
        return new ArrayList<>();
    }

    @Override
    public ByteArrayResource downloadFile(String fileName) {
        log.info("Downloading file: {}", fileName);
        return null;
    }

    @Override
    public boolean deleteFile(String fileName) {
        log.info("Deleting file: {}", fileName);
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        log.info("File deleted successfully: {}", fileName);
        return true;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        log.info("Uploading file: {}", uniqueFileName);
        s3client.putObject(bucketName, uniqueFileName, multipartFile.getInputStream(), null);
        log.info("File uploaded successfully: {}", uniqueFileName);
        return uniqueFileName;
    }

    private String generateUrl(String fileName) {
        log.debug("Generating URL for file: {} with method: {}", fileName, HttpMethod.GET);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1); // Generated URL will be valid for 24 hours
        String url = s3client.generatePresignedUrl(bucketName, fileName, calendar.getTime(), HttpMethod.GET).toString();
        log.debug("Generated URL: {}", url);
        return url;
    }

    // @Async
    public String findByName(String fileName) {
        log.info("Finding file by name: {}", fileName);
        if (!s3client.doesObjectExist(bucketName, fileName)) {
            log.warn("File does not exist: {}", fileName);
            return "File does not exist";
        }
        return generateUrl(fileName);
    }

    public Car generateCarImagesToPreassignedUrls(Car car) {
        log.info("Generating preassigned URLs for car images");
        if (!car.getMainImageUrl().startsWith("https://storage.googleapis.com/")) {
            String preassignedMainImgUrl = findByName(car.getMainImageUrl());
            car.setMainImageUrl(preassignedMainImgUrl);
        }
        List<String> carImageUrls = car.getImagesUrl();
        for (int i = 0; i < carImageUrls.size(); i++) {
            String carImageUrl = carImageUrls.get(i);
            if (!carImageUrl.startsWith("https://storage.googleapis.com/")) {
                carImageUrls.set(i, findByName(carImageUrl));
            }
        }
        log.info("Generated preassigned URLs for car images successfully");
        return car;
    }


}
