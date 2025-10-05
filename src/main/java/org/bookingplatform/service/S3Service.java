package org.bookingplatform.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    String uploadFile(MultipartFile file) throws IOException;

    void deleteFileByUrl(String fileUrl);
}