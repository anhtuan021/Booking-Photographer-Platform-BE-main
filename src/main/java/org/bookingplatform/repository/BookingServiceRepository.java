package org.bookingplatform.repository;

import org.bookingplatform.entity.BookingService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface BookingServiceRepository extends JpaRepository<BookingService, BigInteger> {}
