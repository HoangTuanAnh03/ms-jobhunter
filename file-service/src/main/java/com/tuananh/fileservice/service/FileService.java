package com.tuananh.fileservice.service;

import com.tuananh.fileservice.advice.exception.StorageException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {

    void createDirectory(String folder) throws URISyntaxException;

    void validTypeImage(MultipartFile file) throws StorageException;

    void validTypeResume(MultipartFile file) throws StorageException;

    String store(MultipartFile file, String folder) throws URISyntaxException, IOException, StorageException;

    long getFileLength(String fileName, String folder) throws URISyntaxException;

    InputStreamResource getResource(String fileName, String folder) throws URISyntaxException, FileNotFoundException;
}
