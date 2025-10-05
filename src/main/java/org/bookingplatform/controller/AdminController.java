package org.bookingplatform.controller;

import lombok.RequiredArgsConstructor;
import org.bookingplatform.config.APIRoute;
import org.bookingplatform.dto.BaseRequest;
import org.bookingplatform.dto.BaseResponse;
import org.bookingplatform.dto.customer.CreateCustomerProfileRequest;
import org.bookingplatform.dto.customer.CustomerProfileResponse;
import org.bookingplatform.dto.user.UpdateUserStatusRequest;
import org.bookingplatform.service.AdminService;
import org.bookingplatform.service.CustomerProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.UUID;

@RestController
@RequestMapping(APIRoute.ADMIN)
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final CustomerProfileService customerService;

    // Update admin profile
    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<CustomerProfileResponse>> updateAdminProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "dateOfBirth", required = false) String dateOfBirth,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "ward", required = false) String ward,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            CreateCustomerProfileRequest request = new CreateCustomerProfileRequest();
            request.setDateOfBirth(dateOfBirth);
            request.setGender(gender);
            request.setAddress(address);
            request.setCity(city);
            request.setWard(ward);
            request.setFirstName(firstName);
            request.setLastName(lastName);
            request.setPhone(phone);

            CustomerProfileResponse response = customerService.updateCustomerProfile(token, request, avatarFile);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "03010001", e.getMessage()));
        }
    }

    // Get admin profile
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<CustomerProfileResponse>> getAdminProfile(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            CustomerProfileResponse response = customerService.getProfile(token);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "03010001", e.getMessage()));
        }
    }

    @PatchMapping("/users/status/{userId}")
    public ResponseEntity<BaseResponse<Void>> updateUserStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable BigInteger userId,
            @RequestBody BaseRequest<UpdateUserStatusRequest> request
    ) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();
            adminService.updateUserStatus(token, userId, request.getRequestParameters().getStatus());
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "03010002", e.getMessage()));
        }
    }

}
