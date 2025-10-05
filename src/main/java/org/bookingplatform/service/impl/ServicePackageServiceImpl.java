package org.bookingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookingplatform.dto.photographer.CreateServicePackageRequest;
import org.bookingplatform.dto.photographer.ServicePackageResponse;
import org.bookingplatform.entity.ServicePackage;
import org.bookingplatform.entity.Speciality;
import org.bookingplatform.entity.UserProfile;
import org.bookingplatform.repository.*;
import org.bookingplatform.service.JwtService;
import org.bookingplatform.service.ServicePackageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ServicePackageServiceImpl implements ServicePackageService {
    private final UserProfileRepository userProfileRepository;
    private final SpecialityRepository specialityRepository;
    private final PhotographerSpecialityRepository photographerSpecialityRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final JwtService jwtService;

    @Override
    public ServicePackageResponse createServicePackage(String token, CreateServicePackageRequest request) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        // Validate speciality tồn tại
        Speciality speciality = specialityRepository.findByCode(request.getSpecialityCode())
                .orElseThrow(() -> new RuntimeException("Speciality not found: " + request.getSpecialityCode()));

        List<String> photographerSpecialties = photographerSpecialityRepository.findByPhotographer_User_Id(userIdBigInt)
                .stream()
                .map(ps -> ps.getSpeciality().getCode())
                .toList();

        if (!photographerSpecialties.contains(request.getSpecialityCode())) {
            throw new RuntimeException("This speciality is not offered by the photographer");
        }

        ServicePackage servicePackage = new ServicePackage();
        servicePackage.setCode(request.getCode());
        servicePackage.setName(request.getName());
        servicePackage.setDescription(request.getDescription());
        servicePackage.setBasePrice(request.getBasePrice());
        servicePackage.setMaxPhotos(request.getMaxPhotos());
        servicePackage.setSpeciality(speciality);
        servicePackage.setIsActive(true);

        ServicePackage savedPackage = servicePackageRepository.save(servicePackage);

        return mapToServicePackageResponse(savedPackage);
    }

    private ServicePackageResponse mapToServicePackageResponse(ServicePackage servicePackage) {
        return ServicePackageResponse.builder()
                .id(servicePackage.getId())
                .specialityId(servicePackage.getSpeciality().getId())
                .speciality(servicePackage.getSpeciality().getCode())
                .code(servicePackage.getCode())// Fix: use code
                .name(servicePackage.getName())
                .description(servicePackage.getDescription())
                .basePrice(servicePackage.getBasePrice())
                .maxPhotos(servicePackage.getMaxPhotos())
                .isActive(servicePackage.getIsActive())
                .createdAt(servicePackage.getCreatedAt())
                .updatedAt(servicePackage.getUpdatedAt())
                .isPremium(servicePackage.getIsPremium())
                .discount(servicePackage.getDiscount())
                .build();
    }

    @Override
    public void deleteServicePackage(String token, BigInteger packageId) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        ServicePackage servicePackage = servicePackageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Service package not found"));

        boolean ownsPackage = photographerSpecialityRepository
                .existsByPhotographer_IdAndSpeciality_Id(profile.getId(), servicePackage.getSpeciality().getId());

        if (!ownsPackage) {
            throw new RuntimeException("You can only update your own service packages");
        }

        servicePackageRepository.delete(servicePackage);
    }

    // Get All my service packages
    @Override
    @Transactional(readOnly = true)
    public List<ServicePackageResponse> getMyServicePackages(String token, Boolean isActive) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        List<ServicePackage> packages = servicePackageRepository
                .findActiveByPhotographerProfileId(profile.getId(), isActive != null ? isActive : true);

        return packages.stream()
                .map(this::mapToServicePackageResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ServicePackageResponse getServicePackageById(BigInteger packageId) {
        ServicePackage servicePackage = servicePackageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Service package not found"));

        return mapToServicePackageResponse(servicePackage);
    }

    @Override
    public List<ServicePackageResponse> getAll() {
        List<ServicePackage> packages = servicePackageRepository.findAll();
        return packages.stream()
                .map(this::mapToServicePackageResponse)
                .collect(Collectors.toList());
    }
}
