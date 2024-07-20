package com.mycoolcar.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.mycoolcar.entities.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class AwsS3ServiceImpl implements FileService {

    private AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String bucketRegion;

    public AwsS3ServiceImpl(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public S3Object getFile(String keyName) {
        return s3client.getObject(bucketName, keyName);
    }

    @Override
    public List<String> listOfFiles() {
        return new ArrayList<>();
    }

    @Override
    public ByteArrayResource downloadFile(String fileName) {
        return null;
    }

    @Override
    public boolean deleteFile(String fileName) {
        try {
            s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        s3client.putObject(bucketName, uniqueFileName, multipartFile.getInputStream(), null);
        return uniqueFileName;
    }

    private String generateUrl(String fileName, HttpMethod httpMethod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1); // Generated URL will be valid for 24 hours
        return s3client.generatePresignedUrl(bucketName, fileName, calendar.getTime(), httpMethod).toString();
    }

    @Async
    public String findByName(String fileName) {
        if (!s3client.doesObjectExist(bucketName, fileName))
            return "File does not exist";
        return generateUrl(fileName, HttpMethod.GET);
    }

    public Car generateCarImagesToPreassignedUrls(Car car) {

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
        return car;
    }


}
