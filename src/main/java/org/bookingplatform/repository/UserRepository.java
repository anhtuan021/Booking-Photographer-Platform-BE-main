package org.bookingplatform.repository;

import lombok.NonNull;
import org.bookingplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, BigInteger>, JpaSpecificationExecutor<User> {
    @NonNull
    Optional<User> findById(@NonNull BigInteger id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
