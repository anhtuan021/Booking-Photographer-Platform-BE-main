package org.bookingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.bookingplatform.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String key = "portfolio/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        // Create PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        // Upload file
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        // Return the file URL
        return "https://" + bucketName + ".s3." +
                s3Client.serviceClientConfiguration().region().id() +
                ".amazonaws.com/" + key;
    }

    @Override
    public void deleteFileByUrl(String fileUrl) {
        // Extract the key from the URL
        String key = fileUrl.substring(fileUrl.indexOf(".com/") + 5);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}
