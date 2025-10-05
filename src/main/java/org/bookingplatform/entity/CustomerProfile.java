package org.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bookingplatform.constant.Gender;

import java.time.LocalDate;

@Entity
@Table(name = "customer_profiles", indexes = {
    @Index(name = "idx_customer_user", columnList = "user_id"),
    @Index(name = "idx_customer_city", columnList = "city")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String city;
    private String ward;

    private Boolean phoneVerified = false;
    private Boolean emailVerified = false;
}