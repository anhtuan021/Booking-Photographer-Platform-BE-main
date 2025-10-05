package org.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.bookingplatform.constant.ProfileStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_profiles", indexes = {
    @Index(name = "idx_photographer_user", columnList = "user_id"),
    @Index(name = "idx_photographer_status", columnList = "status"),
    @Index(name = "idx_photographer_location", columnList = "city")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserProfile extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    
    private String businessName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "min_price")
    private BigDecimal minPrice;
    
    private Integer yearsExperience = 0;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    @Column(columnDefinition = "JSON")
    private String languages; // JSON array
    
    @Column(columnDefinition = "TEXT")
    private String locationAddress;

    private String city;

    private String ward;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileStatus status = ProfileStatus.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String approvalNotes;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;
    
    private LocalDateTime approvedAt;

    @OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PortfolioImage> portfolioImages = new ArrayList<>();
}
