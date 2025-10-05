package org.bookingplatform.service;

import java.math.BigInteger;

public interface AdminService {

    void updateUserStatus(String token, BigInteger userId, String status);

}
