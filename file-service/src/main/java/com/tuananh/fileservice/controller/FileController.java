package com.tuananh.fileservice.controller;

import com.tuananh.fileservice.advice.exception.StorageException;
import com.tuananh.fileservice.dto.response.ResUploadFileDTO;
import com.tuananh.fileservice.service.FileService;
import com.tuananh.fileservice.util.NameFolders;
import com.tuananh.fileservice.util.annotation.ApiMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/files")
public class FileController {
    FileService fileService;

    @PostMapping("/upload/logo")
    @ApiMessage("Upload image logo")
    public ResponseEntity<ResUploadFileDTO> uploadLogo(
            @RequestParam(name = "file", required = false) MultipartFile file
    ) throws URISyntaxException, IOException, StorageException {
        // valid type file
        fileService.validTypeResume(file);
        // store file
        List<String> uploadFile = Collections.singletonList(this.fileService.store(file, NameFolders.LOGO));

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/upload/coverImage")
    @ApiMessage("Upload cover image")
    public ResponseEntity<ResUploadFileDTO> uploadCoverImage(
            @RequestParam(name = "file", required = false) MultipartFile file
    ) throws URISyntaxException, IOException, StorageException {
        // valid type file
        fileService.validTypeResume(file);
        // store file
        List<String> uploadFile = Collections.singletonList(this.fileService.store(file, NameFolders.COVER_IMAGE));

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/upload/avatar")
    @ApiMessage("Upload image avatar")
    public ResponseEntity<ResUploadFileDTO> uploadAvatar(
            @RequestParam(name = "file", required = false) MultipartFile file
    ) throws URISyntaxException, IOException, StorageException {
        // valid type file
        fileService.validTypeImage(file);
        // store file
        List<String> uploadFile = Collections.singletonList(this.fileService.store(file, NameFolders.AVATAR));

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/upload/multiResume")
    @ApiMessage("Upload multi resume")
    public ResponseEntity<ResUploadFileDTO> uploadMultiFiles(@RequestParam("files") MultipartFile[] files) throws StorageException {
        try {
            List<String> fileNames = new ArrayList<>();

            Arrays.stream(files).forEach(file -> {
                try {
                    fileService.store(file, NameFolders.RESUME);
                } catch (URISyntaxException | IOException | StorageException e) {
                    throw new RuntimeException(e);
                }
                fileNames.add(file.getOriginalFilename());
            });

            ResUploadFileDTO res = new ResUploadFileDTO(fileNames, Instant.now());

            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            throw new StorageException("Fail to upload files! " + e.getMessage());
        }
    }

    @GetMapping("")
    @ApiMessage("Download a file")
    public ResponseEntity<Resource> download(
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "folder", required = false) String folder)
            throws StorageException, URISyntaxException, FileNotFoundException {
        if (fileName == null || folder == null) {
            throw new StorageException("Missing required params : (fileName or folder) in query params.");
        }

        // check file exist (and not a directory)
        long fileLength = this.fileService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw new StorageException("File with name = " + fileName + " not found.");
        }

        // download a file
        InputStreamResource resource = this.fileService.getResource(fileName, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
