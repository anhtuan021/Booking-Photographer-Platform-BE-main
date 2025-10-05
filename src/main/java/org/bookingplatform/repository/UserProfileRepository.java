package org.bookingplatform.repository;

import lombok.NonNull;
import org.bookingplatform.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigInteger;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, BigInteger>, JpaSpecificationExecutor<UserProfile> {
    @NonNull
    Optional<UserProfile> findById(@NonNull BigInteger userId);

    Optional<UserProfile> findByUser_Id(BigInteger userId);
}
