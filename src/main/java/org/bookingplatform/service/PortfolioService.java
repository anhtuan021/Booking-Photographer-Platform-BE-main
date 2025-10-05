package org.bookingplatform.service;

import org.bookingplatform.dto.photographer.PortfolioImageResponse;
import org.bookingplatform.dto.photographer.UploadPortfolioImageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface PortfolioService {
    PortfolioImageResponse uploadPortfolioImage(String token, UploadPortfolioImageRequest request) throws IOException;

    PortfolioImageResponse updatePortfolioImage(
            String token,
            BigInteger imageId,
            UploadPortfolioImageRequest request,
            MultipartFile imageFile);
    void deletePortfolioImage(String token, BigInteger imageId) throws IOException;

    List<PortfolioImageResponse> getPhotographerPortfolio(BigInteger photographerId);

    List<PortfolioImageResponse> getAllMyPortfolioImages(String token);

    List<PortfolioImageResponse> getAllPortfolioImages();
}
