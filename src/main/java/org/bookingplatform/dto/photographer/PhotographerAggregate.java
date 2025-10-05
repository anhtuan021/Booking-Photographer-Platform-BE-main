package org.bookingplatform.dto.photographer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotographerAggregate {
    private BigInteger profileId;
    private Long bookingCount;
    private Number avgRating;
    private Long totalFeedbacks;
}