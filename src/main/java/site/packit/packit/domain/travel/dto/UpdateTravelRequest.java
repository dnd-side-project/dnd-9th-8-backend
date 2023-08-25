package site.packit.packit.domain.travel.dto;

import site.packit.packit.domain.travel.constant.DestinationType;

import java.time.LocalDateTime;

public record UpdateTravelRequest(
        String title,

        LocalDateTime startDate,

        LocalDateTime endDate
) {
}
