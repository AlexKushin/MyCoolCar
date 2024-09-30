package com.mycoolcar.services;

import com.mycoolcar.entities.Car;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<String> listOfFiles();

    ByteArrayResource downloadFile(String fileName) throws IOException;

    boolean deleteFile(String fileName) throws IOException;

    String uploadFile(MultipartFile file) throws IOException;

    Car generateCarImagesToPreassignedUrls(Car car);
}