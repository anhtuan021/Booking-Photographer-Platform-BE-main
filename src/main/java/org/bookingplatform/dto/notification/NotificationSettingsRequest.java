package org.bookingplatform.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingsRequest {
    private Map<String, Boolean> emailSettings;
    private Map<String, Boolean> pushSettings;
    private Map<String, Boolean> smsSettings;
    private Boolean marketingEnabled;
    private Boolean systemEnabled;
}