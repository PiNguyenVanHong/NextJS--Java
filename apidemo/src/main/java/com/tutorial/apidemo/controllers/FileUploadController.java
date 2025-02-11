package com.tutorial.apidemo.controllers;

import com.tutorial.apidemo.models.ResponseObject;
import com.tutorial.apidemo.interfaces.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/api/v1")
public class FileUploadController {
    @Autowired
    private IStorageService storageService;

    @PostMapping("/uploads")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String generatedFileName = this.storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Upload File Successfully", generatedFileName)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/images/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable("fileName") String fileName) {
        try {
            byte[] bytes = this.storageService.readFileContent(fileName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/images")
    public ResponseEntity<ResponseObject> getAllFiles() {
        try {
            List<String> urls = this.storageService.loadAll()
                    .map(path -> {
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "readDetailFile", path.getFileName().toString()).build().toString();
                        return urlPath;
                    }).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "List Files Successfully", urls)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", "List Files Failed", new String[] {})
            );
        }
    }
}
