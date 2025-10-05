package org.bookingplatform.dto.photographer;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class BlockedDateResponse {
    private LocalDate date;
    private String description;
}