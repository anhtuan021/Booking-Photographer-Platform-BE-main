package org.bookingplatform.dto.photographer;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class SimplePortfolioImageResponse {
    private BigInteger id;
    private String imageUrl;
    private String category;
    private String status;
}