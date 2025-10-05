package org.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "service_packages", indexes = {
    @Index(name = "idx_packages_photographer", columnList = "photographer_id"),
    @Index(name = "idx_packages_active", columnList = "is_active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServicePackage extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String code;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    private Integer maxPhotos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speciality_id", nullable = false)
    private Speciality speciality;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium = false;

    @Column(name = "discount", precision = 5, scale = 2, nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;
}
