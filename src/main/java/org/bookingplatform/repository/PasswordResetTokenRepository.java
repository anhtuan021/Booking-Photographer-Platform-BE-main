package org.bookingplatform.repository;

import org.bookingplatform.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, java.math.BigInteger> {

    Optional<PasswordResetToken> findByToken(String token);

    @Query("SELECT prt FROM PasswordResetToken prt WHERE prt.token = :token AND prt.isUsed = false AND prt.expiresAt > :now")
    Optional<PasswordResetToken> findValidToken(@Param("token") String token, @Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE PasswordResetToken prt SET prt.isUsed = true, prt.usedAt = :usedAt WHERE prt.token = :token")
    void markTokenAsUsed(@Param("token") String token, @Param("usedAt") LocalDateTime usedAt);

    @Modifying
    @Query("DELETE FROM PasswordResetToken prt WHERE prt.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}
