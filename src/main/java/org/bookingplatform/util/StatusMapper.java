package org.bookingplatform.util;

import org.bookingplatform.constant.ProfileStatus;
import org.bookingplatform.constant.UserStatus;

public class StatusMapper {

    public static ProfileStatus mapUserStatusToProfileStatus(UserStatus userStatus) {
        switch (userStatus) {
            case ACTIVE:
                return ProfileStatus.ACTIVE;
            case INACTIVE:
                return ProfileStatus.INACTIVE;
            case SUSPENDED:
                return ProfileStatus.SUSPENDED;
            case PENDING_VERIFICATION:
                return ProfileStatus.PENDING;
            default:
                throw new IllegalArgumentException("Unknown UserStatus: " + userStatus);
        }
    }
}