package org.bookingplatform.controller;

import java.util.List;
import java.util.UUID;

import org.bookingplatform.dto.BaseResponse;
import org.bookingplatform.dto.customer.CreateCustomerProfileRequest;
import org.bookingplatform.dto.customer.CustomerProfileResponse;
import org.bookingplatform.service.CustomerProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerProfileService customerService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<CustomerProfileResponse>>> getAllCustomers() {
        try {
            List<CustomerProfileResponse> response = customerService.searchCustomers();
            return ResponseEntity.ok(new BaseResponse<>(
                    UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "03010001", e.getMessage()));
        }
    }

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<CustomerProfileResponse>> updateMyProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "dateOfBirth", required = false) String dateOfBirth,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "ward", required = false) String ward,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
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
            request.setEmail(email);

            CustomerProfileResponse response = customerService.updateCustomerProfile(token, request, avatarFile);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "03010001", e.getMessage()));
        }
    }

}
