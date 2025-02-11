package com.tutorial.apidemo.services;

import com.tutorial.apidemo.interfaces.IStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService {
    private final Path storageDir = Paths.get("uploads");

    public ImageStorageService() {
        try {
            Files.createDirectories(storageDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"jpg", "jpeg", "png", "svg"})
                .contains(fileExtension.trim().toLowerCase());
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            if(file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            if(!isImageFile(file)) {
                throw new RuntimeException("File is not an image");
            }

            float fileSizeInMegabytes = (float) file.getSize() / 1_000_000.0f;

            if(fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be <= 5Mb");
            }

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFilename = UUID.randomUUID().toString().replace("-", "");
            generatedFilename += "." + fileExtension;
            Path destinationFilePath = this.storageDir.resolve(
                    Paths.get(generatedFilename))
                    .normalize().toAbsolutePath();
            if(!destinationFilePath.getParent().equals(this.storageDir.toAbsolutePath())) {
                throw new RuntimeException(
                        "Destination file path does not match storage directory");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return generatedFilename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.storageDir, 1)
                    .filter(path -> !path.equals(this.storageDir))
                    .map(this.storageDir::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load stored files", e);
        }
    }

    @Override
    public byte[] readFileContent(String filename) {
        try {
            Path file = this.storageDir.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return StreamUtils.copyToByteArray(resource.getInputStream());
            } else {
                throw new RuntimeException(
                        "Could not read file: " + filename
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
