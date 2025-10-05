package org.bookingplatform.repository;

import lombok.NonNull;
import org.bookingplatform.entity.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ServicePackageRepository extends JpaRepository<ServicePackage, BigInteger> {
    @Query("""
        SELECT sp FROM ServicePackage sp
        WHERE sp.speciality.code = :speciality
          AND sp.code IN :codes
          AND sp.isActive = true
    """)
    List<ServicePackage> findActiveByUserIdAndSpecialityAndCodes(
            @Param("speciality") String speciality,
            @Param("codes") List<String> codes
    );

    @Query("""
        SELECT ps.photographer.id, sp FROM ServicePackage sp
        JOIN sp.speciality s
        JOIN PhotographerSpeciality ps ON ps.speciality = s
        WHERE ps.photographer.id IN :profileIds
          AND sp.isActive = :isActive
        ORDER BY sp.createdAt DESC
    """)
    List<Object[]> findActiveByPhotographerProfileIdsWithPhotographerId(
            @Param("profileIds") List<BigInteger> profileIds,
            @Param("isActive") Boolean isActive
    );

    @NonNull
    Optional<ServicePackage> findById(@NonNull BigInteger id);

    @Query("""
    SELECT sp FROM ServicePackage sp
    JOIN sp.speciality s
    JOIN PhotographerSpeciality ps ON ps.speciality = s
    WHERE ps.photographer.user.id = :photographerId
      AND sp.isActive = :isActive
    ORDER BY sp.createdAt DESC
    """)
    List<ServicePackage> findByPhotographerIdAndIsActiveOrderByCreatedAtDesc(
            @Param("photographerId") BigInteger photographerId,
            @Param("isActive") Boolean isActive
    );

    @Query("""
    SELECT sp FROM ServicePackage sp
    JOIN sp.speciality s
    JOIN PhotographerSpeciality ps ON ps.speciality = s
    WHERE ps.photographer.id = :profileId
      AND sp.isActive = :isActive
    ORDER BY sp.createdAt DESC
""")
    List<ServicePackage> findActiveByPhotographerProfileId(
            @Param("profileId") BigInteger profileId,
            @Param("isActive") Boolean isActive
    );
}
