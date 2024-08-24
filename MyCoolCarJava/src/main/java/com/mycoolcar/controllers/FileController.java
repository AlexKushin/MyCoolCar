package com.mycoolcar.controllers;

import com.mycoolcar.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value="/getImages/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ByteArrayResource getImage(@PathVariable String imageName) throws IOException {
        return fileService.downloadFile(imageName);

    }
}

