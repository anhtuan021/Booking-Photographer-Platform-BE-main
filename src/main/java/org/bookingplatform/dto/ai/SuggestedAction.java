package org.bookingplatform.dto.ai;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class SuggestedAction {
    private String action;
    private String description;
    private String url;
}
