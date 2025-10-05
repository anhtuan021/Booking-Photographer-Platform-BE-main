package org.bookingplatform.dto.customer;

import lombok.Data;

@Data
public class CreateCustomerProfileRequest {
    private String dateOfBirth;
    private String gender;
    private String address;
    private String city;
    private String ward;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}