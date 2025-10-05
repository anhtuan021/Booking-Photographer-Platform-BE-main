package org.bookingplatform.repository;

import lombok.NonNull;
import org.bookingplatform.entity.PortfolioImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface PortfolioImageRepository extends JpaRepository<PortfolioImage, BigInteger>, JpaSpecificationExecutor<PortfolioImage> {
    @NonNull
    List<PortfolioImage> findAll();

    List<PortfolioImage> findByPhotographer_IdInOrderByCreatedAtDesc(List<BigInteger> photographerIds);

    List<PortfolioImage> findByPhotographerIdOrderByDisplayOrderAsc(BigInteger photographerId);

    @Query("SELECT p FROM PortfolioImage p WHERE p.photographer.id = :userId")
    List<PortfolioImage> findAllByUserId(@Param("userId") BigInteger userId);
    
    /**
     * Find portfolio images by photographer ID ordered by creation date
     */
    List<PortfolioImage> findByPhotographerIdOrderByCreatedAtDesc(BigInteger photographerId);
}
