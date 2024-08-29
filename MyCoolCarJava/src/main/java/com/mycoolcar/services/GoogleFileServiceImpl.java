package com.mycoolcar.services;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class GoogleFileServiceImpl implements FileService {

    @Value("${gcp.bucket.name}")
    private String bucketName;

    private final Storage storage;

    @Autowired
    public GoogleFileServiceImpl(Storage storage) {
        this.storage = storage;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public List<String> listOfFiles() {
        log.info("Listing all files in bucket: {}", bucketName);
        List<String> list = new ArrayList<>();
        Page<Blob> blobs = storage.list(bucketName); //returns not ordered list
        for (Blob blob : blobs.iterateAll()) {
            list.add(blob.getName());
        }
        Collections.sort(list);
        log.info("Listed {} files in bucket: {}", list.size(), bucketName);
        return list;
    }

    @Override
    public ByteArrayResource downloadFile(String fileName) {
        log.info("Downloading file: {} from bucket: {}", fileName, bucketName);
        Blob blob = storage.get(bucketName, fileName);
        if (blob == null) {
            log.warn("File: {} not found in bucket: {}", fileName, bucketName);
            throw new ResourceNotFoundException("File not found: " + fileName);
        }
        log.info("File: {} downloaded successfully from bucket: {}", fileName, bucketName);
        return new ByteArrayResource(blob.getContent());
    }

    @Override
    public boolean deleteFile(String fileLink) {
        String fileName = extractFileName(fileLink);
        log.info("Deleting file: {} from bucket: {}", fileName, bucketName);
        BlobId blobId = BlobId.of(bucketName, fileName);
        Blob blob = storage.get(blobId);
        boolean deleted = blob.delete();
        if (deleted) {
            log.info("File: {} deleted successfully from bucket: {}", fileName, bucketName);
        } else {
            log.warn("Failed to delete file: {} from bucket: {}", fileName, bucketName);
        }
        return deleted;
    }

    private String extractFileName(String link) {
        log.debug("Extracting file name from link: {}", link);
        String[] parts = link.split("/");
        String lastPart = parts[parts.length - 1];
        String[] fileNameParts = lastPart.split("\\?");
        String fileName = fileNameParts[0];
        log.debug("Extracted file name: {}", fileName);
        return fileName;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        log.info("Uploading file: {} to bucket: {}", uniqueFileName, bucketName);
        BlobId blobId = BlobId.of(bucketName, uniqueFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        String fileLink = blob.getMediaLink();
        if (fileLink == null) {
            log.warn("Failed to get media link for uploaded file: {}", uniqueFileName);
            return file.getOriginalFilename();
        }
        log.info("File: {} uploaded successfully to bucket: {}", uniqueFileName, bucketName);
        return fileLink;
    }
}