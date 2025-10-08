package org.bookingplatform.repository;

import org.bookingplatform.constant.BookingStatus;
import org.bookingplatform.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, BigInteger>, JpaSpecificationExecutor<Booking> {
    List<Booking> findByCustomerIdOrderByCreatedAtDesc(BigInteger customerId);

    Page<Booking> findByPhotographerIdOrderByCreatedAtDesc(BigInteger photographerId, Pageable pageable);
    
    Page<Booking> findByPhotographerIdAndStatusOrderByCreatedAtDesc(BigInteger photographerId, BookingStatus status, Pageable pageable);

    //Get All Bookings of photographer
    List<Booking> findByPhotographer_IdOrderByCreatedAtDesc(BigInteger photographerId);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.photographer.user.id = :photographerId AND b.date = :date")
    long countBookingsByPhotographerAndDate(@Param("photographerId") BigInteger photographerId, @Param("date") LocalDate date);

    List<Booking> findByPhotographer_IdAndDate(BigInteger photographerId, LocalDate date);
    
    boolean existsByBookingCode(String bookingCode);

    // 1 photographer
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.photographer.id = :photographerId")
    Long countByPhotographerId(@Param("photographerId") BigInteger photographerId);

    // all photographers
    @Query("SELECT b.photographer.id, COUNT(b) " +
            "FROM Booking b " +
            "GROUP BY b.photographer.id")
    List<Object[]> countBookingsForAllPhotographers();
    // ✅ Kiểm tra photographer có bị trùng lịch không
    @Query("""
    SELECT COUNT(b) > 0
    FROM Booking b
    WHERE b.photographer.id = :photographerId
      AND b.date = :date
      AND b.status NOT IN ('CANCELLED', 'REJECTED')
      AND (
        (b.startTime < :endTime AND b.endTime > :startTime)
      )
    """)
    boolean existsOverlappingBooking(
        @Param("photographerId") BigInteger photographerId,
        @Param("date") java.time.LocalDate date,
        @Param("startTime") java.time.LocalTime startTime,
        @Param("endTime") java.time.LocalTime endTime
    );
}
