package com.mycoolcar.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GoogleFileServiceImplTest {

    private GoogleFileServiceImpl googleFileService;

    @BeforeEach
    public void setup() {

        Storage storage = LocalStorageHelper.getOptions().getService();
        googleFileService = new GoogleFileServiceImpl(storage);
        //upload 4 files to storage
        BlobInfo blobInfo1 = BlobInfo.newBuilder(
                BlobId.of("my-mock-bucket", "test4.txt")).build();
        storage.create(blobInfo1, "file content4".getBytes());

        BlobInfo blobInfo2 = BlobInfo.newBuilder(
                BlobId.of("my-mock-bucket", "test1.txt")).build();
        storage.create(blobInfo2, "file content1".getBytes());

        BlobInfo blobInfo3 = BlobInfo.newBuilder(
                BlobId.of("my-mock-bucket", "test2.txt")).build();
        storage.create(blobInfo3, "file content2".getBytes());

        BlobInfo blobInfo4 = BlobInfo.newBuilder(
                BlobId.of("my-mock-bucket", "test3.txt")).build();
        storage.create(blobInfo4, "file content3".getBytes());

        googleFileService.setBucketName("my-mock-bucket");
    }

    @Test
    void testListOfFiles() {
        // Test list of files method
        List<String> files = googleFileService.listOfFiles();

        // Verify results
        assertEquals(4, files.size());

        assertEquals("test1.txt", files.get(0));
        assertEquals("test2.txt", files.get(1));
        assertEquals("test3.txt", files.get(2));
        assertEquals("test4.txt", files.get(3));
    }

    @Test
    void testDownloadFile() {
        // Test download file method
        ByteArrayResource resource = googleFileService.downloadFile("test1.txt");

        // Verify result
        assertNotNull(resource);
        assertEquals("file content1", new String(resource.getByteArray()));
    }

    @Test
    void testDeleteFile() {
        // Test init list size of files method
        List<String> files = googleFileService.listOfFiles();
        assertEquals(4, files.size());

        boolean result = googleFileService.deleteFile("test1.txt");
        assertTrue(result);
        // Test list of files method after deleting
        files = googleFileService.listOfFiles();

        // Verify results
        assertEquals(3, files.size());
    }

    @Test
    void testUploadFile() throws IOException {
        // Mock MultipartFile
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.txt");
        when(file.getContentType()).thenReturn("text/plain");
        when(file.getBytes()).thenReturn("file content".getBytes());

        // Test upload file method
        String result = googleFileService.uploadFile(file);

        assertNotNull(result);
        assertEquals("test.txt", result);
    }
}