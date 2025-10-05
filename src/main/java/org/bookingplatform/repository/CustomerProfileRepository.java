package org.bookingplatform.repository;

import org.bookingplatform.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, BigInteger> {
    Optional<CustomerProfile> findByUser_Id(BigInteger userId);

    @Query("SELECT cp FROM CustomerProfile cp JOIN FETCH cp.user")
    List<CustomerProfile> findAllWithUser();

    @Query("SELECT cp FROM CustomerProfile cp JOIN FETCH cp.user WHERE cp.user.id = :userId")
    Optional<CustomerProfile> findByUserIdWithUser(@Param("userId") BigInteger userId);
}