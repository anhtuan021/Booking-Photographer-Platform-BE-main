package org.bookingplatform.repository;

import org.bookingplatform.entity.PhotographerSpeciality;
import org.bookingplatform.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

public interface PhotographerSpecialityRepository extends JpaRepository<PhotographerSpeciality, BigInteger> {
    @Modifying
    @Transactional
    void deleteByPhotographer(UserProfile photographer);

    List<PhotographerSpeciality> findByPhotographer_User_Id(BigInteger userId);

    boolean existsByPhotographer_IdAndSpeciality_Id(BigInteger photographerId, BigInteger specialityId);

    List<PhotographerSpeciality> findByPhotographer_User_IdIn(List<BigInteger> userIds);
}
