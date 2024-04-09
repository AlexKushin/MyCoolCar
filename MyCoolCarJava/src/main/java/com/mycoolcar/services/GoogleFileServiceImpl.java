package com.mycoolcar.services;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
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

        List<String> list = new ArrayList<>();
        Page<Blob> blobs = storage.list(bucketName); //returns not ordered list
        for (Blob blob : blobs.iterateAll()) {
            list.add(blob.getName());
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public ByteArrayResource downloadFile(String fileName) {
        Blob blob = storage.get(bucketName, fileName);
        return new ByteArrayResource(blob.getContent());
    }

    @Override
    public boolean deleteFile(String fileLink) {
        String fileName = extractFileName(fileLink);
        BlobId blobId = BlobId.of(bucketName, fileName);
        Blob blob = storage.get(blobId);
        return blob.delete();

    }

    private String extractFileName(String link) {
        String[] parts = link.split("/");
        String lastPart = parts[parts.length - 1];
        String[] fileNameParts = lastPart.split("\\?");
        return fileNameParts[0];
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, uniqueFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).
                setContentType(file.getContentType()).build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        String fileLink = blob.getMediaLink();
        if (fileLink == null) {
            return file.getOriginalFilename();
        }
        return fileLink;

    }
}