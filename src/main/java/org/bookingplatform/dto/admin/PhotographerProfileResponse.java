package org.bookingplatform.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotographerProfileResponse {
    private String id;
    private String userId;
    private String businessName;
    private String bio;
    private Integer yearsExperience;
    private List<String> specialties;
    private List<String> languages;
    private String locationAddress;
    private String city;
    private String district;
    private String ward;
    private String status;
    private String approvalNotes;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
