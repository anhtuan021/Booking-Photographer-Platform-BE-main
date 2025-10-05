package org.bookingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookingplatform.constant.ProfileStatus;
import org.bookingplatform.constant.Role;
import org.bookingplatform.constant.UserStatus;
import org.bookingplatform.entity.User;
import org.bookingplatform.entity.UserProfile;
import org.bookingplatform.repository.UserProfileRepository;
import org.bookingplatform.repository.UserRepository;
import org.bookingplatform.service.*;
import org.bookingplatform.util.StatusMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public void updateUserStatus(String token, BigInteger userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserStatus newStatus = UserStatus.valueOf(status);
        user.setStatus(newStatus);
        userRepository.save(user);

        if (user.getRole() == Role.PHOTOGRAPHER) {
            UserProfile userProfile = userProfileRepository.findByUser_Id(userId)
                    .orElseThrow(() -> new RuntimeException("UserProfile not found for photographer"));

            ProfileStatus profileStatus = StatusMapper.mapUserStatusToProfileStatus(newStatus);

            userProfile.setStatus(profileStatus);
            userProfileRepository.save(userProfile);
        }
    }
}
