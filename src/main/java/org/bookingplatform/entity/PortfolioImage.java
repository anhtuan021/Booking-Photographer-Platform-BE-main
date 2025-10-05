package org.bookingplatform.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bookingplatform.constant.ImageCategory;
import org.bookingplatform.constant.ImageStatus;

@Entity
@Table(name = "portfolio_images", indexes = {
    @Index(name = "idx_portfolio_photographer", columnList = "photographer_id"),
    @Index(name = "idx_portfolio_category", columnList = "category"),
    @Index(name = "idx_portfolio_featured", columnList = "is_featured"),
    @Index(name = "idx_portfolio_status", columnList = "status")
})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class PortfolioImage extends BaseEntity {

    @Schema(description = "The photographer's profile who owns this portfolio image")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", nullable = false)
    private UserProfile photographer;

    @Schema(description = "The URL of the main image")
    @Column(columnDefinition = "JSON")
    private String imageUrl;

    @Schema(description = "The title of the image")
    private String title;

    @Schema(description = "A detailed description of the image")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Schema(description = "The category of the image (e.g., PORTRAIT, LANDSCAPE)")
    @Enumerated(EnumType.STRING)
    private ImageCategory category = ImageCategory.OTHER;

    @Schema(description = "Tags for the image, stored as a JSON array")
    @Column(columnDefinition = "JSON")
    private String tags; // JSON array

    @Schema(description = "The order in which the image is displayed in the portfolio", defaultValue = "0")
    private Integer displayOrder = 0;

    @Schema(description = "Indicates if the image is featured in the portfolio", defaultValue = "false")
    private Boolean isFeatured = false;

    @Schema(description = "The approval status of the image (e.g., PENDING_APPROVAL, APPROVED)", defaultValue = "PENDING_APPROVAL")
    @Enumerated(EnumType.STRING)
    private ImageStatus status = ImageStatus.PENDING_APPROVAL;
}
