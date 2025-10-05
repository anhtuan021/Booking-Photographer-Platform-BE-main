package org.bookingplatform.service;

import org.bookingplatform.dto.customer.CreateCustomerProfileRequest;
import org.bookingplatform.dto.customer.CustomerProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerProfileService {
    CustomerProfileResponse updateCustomerProfile(
            String token,
            CreateCustomerProfileRequest request,
            MultipartFile avatarFile);

    CustomerProfileResponse getProfile(String token);

    List<CustomerProfileResponse> searchCustomers();
}