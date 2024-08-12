package com.tuananh.fileservice.controller;

import com.tuananh.fileservice.advice.exception.StorageException;
import com.tuananh.fileservice.dto.ApiResponse;
import com.tuananh.fileservice.dto.response.ResUploadFileDTO;
import com.tuananh.fileservice.service.FileService;
import com.tuananh.fileservice.util.NameFolders;
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
    public ApiResponse<ResUploadFileDTO> uploadLogo(
            @RequestParam(name = "file", required = false) MultipartFile file
    ) throws URISyntaxException, IOException, StorageException {
        // valid type file
        fileService.validTypeResume(file);
        // store file
        List<String> uploadFile = Collections.singletonList(this.fileService.store(file, NameFolders.LOGO));

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ApiResponse.<ResUploadFileDTO>builder()
                .code(HttpStatus.CREATED.value())
                .message("Upload image logo")
                .data(res)
                .build();
    }

    @PostMapping("/upload/coverImage")
    public ApiResponse<ResUploadFileDTO> uploadCoverImage(
            @RequestParam(name = "file", required = false) MultipartFile file
    ) throws URISyntaxException, IOException, StorageException {
        // valid type file
        fileService.validTypeResume(file);
        // store file
        List<String> uploadFile = Collections.singletonList(this.fileService.store(file, NameFolders.COVER_IMAGE));

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ApiResponse.<ResUploadFileDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Upload cover image")
                .data(res)
                .build();
    }

    @PostMapping("/upload/avatar")
    public ApiResponse<ResUploadFileDTO> uploadAvatar(
            @RequestParam(name = "file", required = false) MultipartFile file
    ) throws URISyntaxException, IOException, StorageException {
        // valid type file
        fileService.validTypeImage(file);
        // store file
        List<String> uploadFile = Collections.singletonList(this.fileService.store(file, NameFolders.AVATAR));

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ApiResponse.<ResUploadFileDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Upload image avatar")
                .data(res)
                .build();
    }

    @PostMapping("/upload/multiResume")
    public ApiResponse<ResUploadFileDTO> uploadMultiFiles(@RequestParam("files") MultipartFile[] files) throws StorageException {
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

            return ApiResponse.<ResUploadFileDTO>builder()
                    .code(HttpStatus.CREATED.value())
                    .message("Upload multi resume")
                    .data(res)
                    .build();
        } catch (Exception e) {
            throw new StorageException("Fail to upload files! " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Resource>> download(
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

        ApiResponse<Resource> result = ApiResponse.<Resource>builder()
                .code(HttpStatus.OK.value())
                .message("Download a file")
                .data(resource)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(result);
    }
}
