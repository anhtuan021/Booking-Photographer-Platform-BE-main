package org.bookingplatform.repository;

import java.math.BigInteger;
import java.util.Optional;

import org.bookingplatform.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialityRepository extends JpaRepository<Speciality, BigInteger> {
    Optional<Speciality> findByCode(String code);
}
