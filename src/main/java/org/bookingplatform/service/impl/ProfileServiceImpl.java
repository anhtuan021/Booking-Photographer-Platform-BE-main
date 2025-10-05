package org.bookingplatform.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookingplatform.constant.Role;
import org.bookingplatform.dto.photographer.*;
import org.bookingplatform.entity.*;
import org.bookingplatform.repository.*;
import org.bookingplatform.service.JwtService;
import org.bookingplatform.service.ProfileService;
import org.bookingplatform.service.S3Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static org.bookingplatform.mapper.ProfileMapper.mapToProfileResponse;
import static org.bookingplatform.util.ObjectMapperUtil.fromJson;
import static org.bookingplatform.util.ObjectMapperUtil.toJson;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfileServiceImpl implements ProfileService {
    private final UserProfileRepository userProfileRepository;
    private final PhotographerSpecialityRepository photographerSpecialityRepository;
    private final SpecialityRepository specialityRepository;
    private final BookingRepository bookingRepository;
    private final FeedbackRepository feedbackRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final PortfolioImageRepository portfolioImageRepository;
    private final JwtService jwtService;
    private final S3Service s3Service;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PhotographerProfileResponse updatePhotographerProfile(
            String token,
            CreatePhotographerProfileRequest request,
            MultipartFile avatarFile
    ) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String avatarUrl = s3Service.uploadFile(avatarFile);

                profile.setAvatarUrl(avatarUrl);

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload avatar", e);
            }
        }

        profile.setBusinessName(request.getBusinessName());
        profile.setBio(request.getBio());
        profile.setMinPrice(request.getMinPrice());
        profile.setYearsExperience(request.getYearsExperience());
        profile.setLocationAddress(request.getLocationAddress());
        profile.setCity(request.getCity());
        profile.setWard(request.getWard());
        profile.setLanguages(toJson(request.getLanguages()));

        if (request.getDateOfBirth() != null && !request.getDateOfBirth().isEmpty()) {
            LocalDate dob = null;
            DateTimeParseException lastException = null;
            for (String pattern : new String[]{"dd/MM/yyyy", "MM/dd/yyyy"}) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                    dob = LocalDate.parse(request.getDateOfBirth(), formatter);
                    break;
                } catch (DateTimeParseException e) {
                    lastException = e;
                }
            }
            if (dob == null) {
                throw new RuntimeException("Invalid dateOfBirth format. Expected dd/MM/yyyy or MM/dd/yyyy", lastException);
            }
            profile.setDateOfBirth(dob);
        }

        // Update username
        User user = profile.getUser();
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        // Update specialties
        // Remove old specialties
        photographerSpecialityRepository.deleteByPhotographer(profile);
        photographerSpecialityRepository.flush(); // Ensure delete is executed in DB
        entityManager.flush();
        entityManager.clear();

        // Add new specialties (unique only)
        if (request.getSpecialties() != null) {
            request.getSpecialties().stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()  // remove duplicates
                    .forEach(specialityCode -> {
                        Speciality speciality = specialityRepository.findByCode(specialityCode)
                                .orElseThrow(() -> new RuntimeException("Speciality not found: " + specialityCode));
                        PhotographerSpeciality ps = new PhotographerSpeciality();
                        ps.setPhotographer(profile);
                        ps.setSpeciality(speciality);
                        photographerSpecialityRepository.save(ps);
                    });
        }
        UserProfile savedProfile = userProfileRepository.save(profile);

        // Fetch specialties to include in response
        List<PhotographerSpeciality> specialityEntities = photographerSpecialityRepository.findByPhotographer_User_Id(savedProfile.getUser().getId());
        List<String> specialties = specialityEntities.stream()
                .map(ps -> ps.getSpeciality().getCode())
                .toList();

        return mapToProfileResponse(savedProfile, specialties);
    }

    @Override
    public PhotographerProfileResponse getPhotographerProfile(String token) {
        String userId = jwtService.extractUserId(token);
        BigInteger userIdBigInt = new BigInteger(userId);

        UserProfile profile = userProfileRepository.findByUser_Id(userIdBigInt)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        return buildPhotographerProfileResponse(profile,
                bookingRepository.countByPhotographerId(profile.getId()),
                feedbackRepository.getAverageRatingByPhotographerId(profile.getId()),
                feedbackRepository.countTotalFeedbacksByPhotographerId(profile.getId()));
    }

    @Override
    public PhotographerProfileResponse getPhotographerProfileById(BigInteger photographerId) {
        UserProfile profile = userProfileRepository.findById(photographerId)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        return buildPhotographerProfileResponse(profile,
                bookingRepository.countByPhotographerId(profile.getId()),
                feedbackRepository.getAverageRatingByPhotographerId(profile.getId()),
                feedbackRepository.countTotalFeedbacksByPhotographerId(profile.getId()));
    }

    @Override
    public List<PhotographerProfileResponse> findAllPhotographers() {
        // Preload statistics in batch
        Map<BigInteger, Long> bookingsCountMap = bookingRepository.countBookingsForAllPhotographers()
                .stream()
                .collect(Collectors.toMap(
                        row -> (BigInteger) row[0],
                        row -> (Long) row[1]
                ));

        Map<BigInteger, Double> avgRatingMap = feedbackRepository.getAverageRatingsForAllPhotographers()
                .stream()
                .collect(Collectors.toMap(
                        row -> (BigInteger) row[0],
                        row -> (Double) row[1]
                ));

        Map<BigInteger, Long> feedbackCountMap = feedbackRepository.countFeedbacksForAllPhotographers()
                .stream()
                .collect(Collectors.toMap(
                        row -> (BigInteger) row[0],
                        row -> (Long) row[1]
                ));

        return userProfileRepository.findAll().stream()
                .filter(profile -> profile.getUser().getRole() == Role.PHOTOGRAPHER)
                .map(profile -> buildPhotographerProfileResponse(
                        profile,
                        bookingsCountMap.getOrDefault(profile.getId(), 0L),
                        avgRatingMap.getOrDefault(profile.getId(), 0.0),
                        feedbackCountMap.getOrDefault(profile.getId(), 0L)
                ))
                .collect(Collectors.toList());
    }

    /**
     * Build PhotographerProfileResponse with injected statistics
     */
    private PhotographerProfileResponse buildPhotographerProfileResponse(
            UserProfile profile,
            Long totalBookings,
            Double averageRating,
            Long totalFeedbacks
    ) {
        // Specialties
        List<PhotographerSpeciality> specialityEntities =
                photographerSpecialityRepository.findByPhotographer_User_Id(profile.getUser().getId());
        List<String> specialties = specialityEntities.stream()
                .map(ps -> ps.getSpeciality().getCode())
                .toList();

        // Service packages
        List<ServicePackageResponse> servicePackages =
                servicePackageRepository.findByPhotographerIdAndIsActiveOrderByCreatedAtDesc(profile.getId(), true)
                        .stream()
                        .map(this::mapToServicePackageResponse)
                        .collect(Collectors.toList());

        // Portfolio images
        List<SimplePortfolioImageResponse> portfolioImages =
                portfolioImageRepository.findByPhotographerIdOrderByCreatedAtDesc(profile.getId())
                        .stream()
                        .map(this::mapToPortfolioImageResponse)
                        .collect(Collectors.toList());

        return PhotographerProfileResponse.builder()
                .id(profile.getId())
                .photographerId(profile.getUser().getId())
                .businessName(profile.getBusinessName())
                .email(profile.getUser().getEmail())
                .dateOfBirth(profile.getDateOfBirth() != null
                        ? profile.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : null)
                .lastName(profile.getUser().getLastName())
                .firstName(profile.getUser().getFirstName())
                .bio(profile.getBio())
                .minPrice(profile.getMinPrice())
                .yearsExperience(profile.getYearsExperience())
                .locationAddress(profile.getLocationAddress())
                .city(profile.getCity())
                .ward(profile.getWard())
                .status(profile.getStatus().name())
                .avatarUrl(profile.getAvatarUrl())
                .languages(fromJson(profile.getLanguages()))
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .specialties(specialties)
                .averageRating(averageRating)
                .totalBookings(totalBookings != null ? totalBookings.intValue() : 0)
                .totalFeedbacks(totalFeedbacks != null ? totalFeedbacks : 0L)
                .servicePackages(servicePackages)
                .portfolioImages(portfolioImages)
                .build();
    }

    @Override
    public List<PhotographerProfileResponse> search(PhotographerSearchRequest req) {
        // Build Specification
        Specification<UserProfile> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<UserProfile, User> userJoin = root.join("user", JoinType.INNER);
            predicates.add(cb.equal(userJoin.get("role"), Role.PHOTOGRAPHER));

            if (req.getId() != null) {
                predicates.add(cb.equal(root.get("id"), req.getId()));
            }
            if (StringUtils.hasText(req.getCity())) {
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + req.getCity().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(req.getWard())) {
                predicates.add(cb.like(cb.lower(root.get("ward")), "%" + req.getWard().toLowerCase() + "%"));
            }
            if (req.getYearsExperience() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("yearsExperience"), req.getYearsExperience()));
            }
            if (req.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("minPrice"), req.getMinPrice()));
            }
            if (req.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("maxPrice"), req.getMaxPrice()));
            }
            if (req.getMinRating() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("averageRating"), req.getMinRating()));
            }

            if (StringUtils.hasText(req.getKeyword())) {
                String[] words = req.getKeyword().trim().toLowerCase().split("\\s+");
                List<Predicate> wordPredicates = new ArrayList<>();

                for (String word : words) {
                    String likeWord = "%" + word + "%";

                    // Base string search
                    Predicate perWord = cb.or(
                            cb.like(cb.lower(root.get("businessName")), likeWord),
                            cb.like(cb.lower(root.get("bio")), likeWord),
                            cb.like(cb.lower(root.get("locationAddress")), likeWord),
                            cb.like(cb.lower(root.get("city")), likeWord),
                            cb.like(cb.lower(root.get("ward")), likeWord),
                            cb.like(cb.lower(userJoin.get("email")), likeWord)
                    );

                    // Extra: if "word" is numeric string, search yearsExperience
                    try {
                        BigDecimal number = new BigDecimal(word);
                        Predicate numericMatch = cb.or(
                                cb.greaterThanOrEqualTo(root.get("minPrice"), number),      // minPrice >= number
                                cb.greaterThanOrEqualTo(root.get("yearsExperience"), number) // yearsExperience >= number
                        );
                        perWord = cb.or(perWord, numericMatch);
                    } catch (NumberFormatException ignored) {
                        // if not a number, do nothing
                    }

                    wordPredicates.add(perWord);
                }
                predicates.add(cb.and(wordPredicates.toArray(new Predicate[0])));
            }
            if (req.getCreate_at() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), req.getCreate_at().atStartOfDay()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // Sort
        Sort sort = Sort.unsorted();
        if (StringUtils.hasText(req.getSortBy())) {
            Sort.Direction direction = "desc".equalsIgnoreCase(req.getSortOrder())
                    ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, req.getSortBy());
        }

        // Query profiles
        List<UserProfile> profiles = userProfileRepository.findAll(spec, sort);
        if (profiles.isEmpty()) return Collections.emptyList();

        List<BigInteger> profileIds = profiles.stream().map(UserProfile::getId).toList();
        List<BigInteger> userIds = profiles.stream().map(p -> p.getUser().getId()).toList();

        // Query aggregates in batch
        Map<BigInteger, PhotographerAggregate> aggregates = feedbackRepository.findAggregates(profileIds).stream()
                .collect(Collectors.toMap(PhotographerAggregate::getProfileId, agg -> agg));

        // Query specialties in batch
        Map<BigInteger, List<String>> specialtiesMap = photographerSpecialityRepository.findByPhotographer_User_IdIn(userIds).stream()
                .collect(Collectors.groupingBy(
                        ps -> ps.getPhotographer().getUser().getId(),
                        Collectors.mapping(ps -> ps.getSpeciality().getCode(), Collectors.toList())
                ));

        // Query service packages in batch
        Map<BigInteger, List<ServicePackageResponse>> servicePackageMap =
                servicePackageRepository.findActiveByPhotographerProfileIdsWithPhotographerId(profileIds, true).stream()
                        .collect(Collectors.groupingBy(
                                row -> (BigInteger) row[0],
                                Collectors.mapping(row -> mapToServicePackageResponse((ServicePackage) row[1]), Collectors.toList())
                        ));

        Map<BigInteger, List<SimplePortfolioImageResponse>> portfolioMap =
                portfolioImageRepository.findByPhotographer_IdInOrderByCreatedAtDesc(profileIds).stream()
                        .collect(Collectors.groupingBy(
                                pf -> pf.getPhotographer().getId(),
                                Collectors.mapping(this::mapToPortfolioImageResponse, Collectors.toList())
                        ));

        // Build response
        return profiles.stream()
                .map(profile -> {
                    PhotographerAggregate agg = aggregates.getOrDefault(
                            profile.getId(),
                            new PhotographerAggregate(profile.getId(), 0L, 0.0, 0L)
                    );

                    return PhotographerProfileResponse.builder()
                            .id(profile.getId())
                            .photographerId(profile.getUser().getId())
                            .businessName(profile.getBusinessName())
                            .email(profile.getUser().getEmail())
                            .dateOfBirth(profile.getDateOfBirth() != null
                                    ? profile.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    : null)
                            .lastName(profile.getUser().getLastName())
                            .firstName(profile.getUser().getFirstName())
                            .bio(profile.getBio())
                            .minPrice(profile.getMinPrice())
                            .yearsExperience(profile.getYearsExperience())
                            .locationAddress(profile.getLocationAddress())
                            .city(profile.getCity())
                            .ward(profile.getWard())
                            .status(profile.getStatus().name())
                            .avatarUrl(profile.getAvatarUrl())
                            .languages(fromJson(profile.getLanguages()))
                            .createdAt(profile.getCreatedAt())
                            .updatedAt(profile.getUpdatedAt())
                            .specialties(specialtiesMap.getOrDefault(profile.getUser().getId(), List.of()))
                            .averageRating(agg.getAvgRating() != null ? agg.getAvgRating().doubleValue() : 0.0)
                            .totalBookings(agg.getBookingCount().intValue())
                            .totalFeedbacks(agg.getTotalFeedbacks())
                            .servicePackages(servicePackageMap.getOrDefault(profile.getId(), List.of()))
                            .portfolioImages(portfolioMap.getOrDefault(profile.getId(), List.of()))
                            .build();
                })
                .toList();
    }
    
    private ServicePackageResponse mapToServicePackageResponse(ServicePackage servicePackage) {
        return ServicePackageResponse.builder()
                .id(servicePackage.getId())
                .name(servicePackage.getName())
                .code(servicePackage.getCode())
                .description(servicePackage.getDescription())
                .basePrice(servicePackage.getBasePrice())
                .maxPhotos(servicePackage.getMaxPhotos())
                .isActive(servicePackage.getIsActive())
                .speciality(servicePackage.getSpeciality().getCode())
                //.specialityName(servicePackage.getSpeciality().getName())
                .createdAt(servicePackage.getCreatedAt())
                .build();
    }
    
    private SimplePortfolioImageResponse mapToPortfolioImageResponse(PortfolioImage portfolioImage) {
        return SimplePortfolioImageResponse.builder()
                .id(portfolioImage.getId())
                .imageUrl(portfolioImage.getImageUrl())
                .category(portfolioImage.getCategory().name())
                .status(portfolioImage.getStatus().name())
                //.createdAt(portfolioImage.getCreatedAt())
                .build();
    }
}
