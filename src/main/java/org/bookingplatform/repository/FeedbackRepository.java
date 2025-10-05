package org.bookingplatform.repository;

import org.bookingplatform.dto.photographer.PhotographerAggregate;
import org.bookingplatform.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, BigInteger> {
    // 1 photographer
    @Query("SELECT AVG(f.rating) FROM Feedback f " +
            "WHERE f.photographer.id = :photographerId AND f.isApproved = true")
    Double getAverageRatingByPhotographerId(@Param("photographerId") BigInteger photographerId);

    @Query("SELECT COUNT(f) FROM Feedback f " +
            "WHERE f.photographer.id = :photographerId AND f.isApproved = true")
    Long countTotalFeedbacksByPhotographerId(@Param("photographerId") BigInteger photographerId);

    // all photographers
    @Query("SELECT f.photographer.id, AVG(f.rating) " +
            "FROM Feedback f " +
            "WHERE f.isApproved = true " +
            "GROUP BY f.photographer.id")
    List<Object[]> getAverageRatingsForAllPhotographers();

    @Query("SELECT f.photographer.id, COUNT(f) " +
            "FROM Feedback f " +
            "WHERE f.isApproved = true " +
            "GROUP BY f.photographer.id")
    List<Object[]> countFeedbacksForAllPhotographers();
    
    // Find all feedbacks with eager loading
    @Query("SELECT f FROM Feedback f " +
           "LEFT JOIN FETCH f.booking b " +
           "LEFT JOIN FETCH f.customer c " +
           "LEFT JOIN FETCH f.photographer p " +
           "LEFT JOIN FETCH p.user " +
           "LEFT JOIN FETCH f.approvedBy " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findAllWithDetails();
    
    // Find feedbacks by customer with eager loading
    @Query("SELECT f FROM Feedback f " +
           "LEFT JOIN FETCH f.booking b " +
           "LEFT JOIN FETCH f.customer c " +
           "LEFT JOIN FETCH f.photographer p " +
           "LEFT JOIN FETCH p.user " +
           "LEFT JOIN FETCH f.approvedBy " +
           "WHERE f.customer.id = :customerId " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByCustomerIdWithDetails(@Param("customerId") BigInteger customerId);
    
    // Find feedbacks by photographer with eager loading
    @Query("SELECT f FROM Feedback f " +
           "LEFT JOIN FETCH f.booking b " +
           "LEFT JOIN FETCH f.customer c " +
           "LEFT JOIN FETCH f.photographer p " +
           "LEFT JOIN FETCH p.user " +
           "LEFT JOIN FETCH f.approvedBy " +
           "WHERE f.photographer.id = :photographerId AND f.isApproved = :isApproved " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByPhotographerIdAndIsApprovedWithDetails(@Param("photographerId") BigInteger photographerId, @Param("isApproved") Boolean isApproved);

    @Query("""
    SELECT new org.bookingplatform.dto.photographer.PhotographerAggregate(
        p.id,
        COUNT(DISTINCT b.id),
        COALESCE(AVG(f.rating), CAST(0.0 AS double)),
        COUNT(DISTINCT f.id)
    )
    FROM UserProfile p
    LEFT JOIN Booking b ON b.photographer.id = p.id
    LEFT JOIN Feedback f ON f.photographer.id = p.id
    WHERE p.id IN :profileIds
    GROUP BY p.id
    """)
    List<PhotographerAggregate> findAggregates(@Param("profileIds") List<BigInteger> profileIds);
}
