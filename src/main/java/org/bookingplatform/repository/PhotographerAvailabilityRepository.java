package org.bookingplatform.repository;

import org.bookingplatform.entity.PhotographerAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PhotographerAvailabilityRepository extends JpaRepository<PhotographerAvailability, BigInteger> {
    
    /**
     * Find blocked dates for a photographer
     */
    List<PhotographerAvailability> findByPhotographerIdOrderByBlockedDateAsc(BigInteger photographerId);
    
    /**
     * Find blocked dates for a photographer within a date range
     */
    @Query("SELECT pa FROM PhotographerAvailability pa WHERE pa.photographer.id = :photographerId " +
           "AND pa.blockedDate BETWEEN :startDate AND :endDate " +
           "ORDER BY pa.blockedDate ASC")
    List<PhotographerAvailability> findByPhotographerIdAndDateRange(
            @Param("photographerId") BigInteger photographerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Check if a specific date is blocked for a photographer
     */
    boolean existsByPhotographerIdAndBlockedDate(BigInteger photographerId, LocalDate blockedDate);
    
    /**
     * Delete specific blocked date for a photographer
     */
    void deleteByPhotographerIdAndBlockedDate(BigInteger photographerId, LocalDate blockedDate);
}
