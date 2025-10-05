package org.bookingplatform.dto.customer;

import lombok.Builder;
import lombok.Data;
import org.bookingplatform.constant.Gender;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@Builder
public class CustomerProfileResponse {
    private BigInteger id;
    private BigInteger userId;
    private String dateOfBirth;
    private String avatarUrl;
    private Gender gender;
    private String address;
    private String city;
    private String ward;
    private Boolean phoneVerified;
    private Boolean emailVerified;
    private String firstName;
    private String lastName;
    // private String email;
    private String phone;
}