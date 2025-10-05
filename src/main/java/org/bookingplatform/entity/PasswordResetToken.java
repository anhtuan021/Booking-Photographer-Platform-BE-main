package org.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens", indexes = {
    @Index(name = "idx_reset_token", columnList = "token"),
    @Index(name = "idx_reset_user", columnList = "user_id")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordResetToken extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean isUsed = false;

    @Column
    private LocalDateTime usedAt;
}
