package org.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.bookingplatform.constant.Role;
import org.bookingplatform.constant.UserStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_type_status", columnList = "role, status"),
        @Index(name = "idx_users_social", columnList = "social_login_provider, social_login_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    private String passwordHash;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.PENDING_VERIFICATION;

    private Boolean emailVerified = false;

    private Boolean phoneVerified = false;

    private String socialLoginProvider;

    private String socialLoginId;

    private LocalDateTime lastLoginAt;
}
