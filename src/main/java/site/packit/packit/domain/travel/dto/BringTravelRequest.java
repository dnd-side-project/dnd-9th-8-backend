package site.packit.packit.domain.travel.dto;


import java.time.LocalDateTime;
import site.packit.packit.domain.travel.constant.DestinationType;

import java.time.LocalDateTime;

public record BringTravelRequest(
        Long memberId,

        String title,

        LocalDateTime startDate,

        LocalDateTime endDate
) {
