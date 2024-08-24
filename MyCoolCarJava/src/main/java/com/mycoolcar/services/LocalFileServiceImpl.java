package com.mycoolcar.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class LocalFileServiceImpl implements FileService {

    @Value("${local.storage.images}")
    private String uploadDirectory;
    @Value("${server.port}")
    private int serverPort;

    @Override
    public List<String> listOfFiles() {
        return List.of();
    }

    @Override
    public ByteArrayResource downloadFile(String fileName) throws IOException {
        Path imagePath = Path.of(uploadDirectory, fileName);

        if (Files.exists(imagePath)) {
            return new ByteArrayResource(Files.readAllBytes(imagePath));
        } else {
            return null; // Handle missing images
        }


    }

    @Override
    public boolean deleteFile(String fileName) throws IOException {
        Path imagePath = Path.of(uploadDirectory, fileName);
        log.info("image has been deleted by path {}", imagePath);
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            return true;
        } else {
            return false; // Handle missing images
        }
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Path.of(uploadDirectory);
        Path filePath = uploadPath.resolve(uniqueFileName);
        log.info("upload directory {}", uploadDirectory);
        //todo add opportunity to create folders for every user, by user name/login/nickname
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        log.info("image has been stored by path {}", filePath);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "http://localhost:" + serverPort + "/getImages/" + uniqueFileName;


    }
}
