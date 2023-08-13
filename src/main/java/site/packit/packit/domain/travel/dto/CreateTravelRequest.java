package site.packit.packit.domain.travel.dto;

import site.packit.packit.domain.travel.constant.DestinationType;

import java.time.LocalDateTime;

public record CreateTravelRequest(

        Long memberId,

        String title,

        DestinationType destinationType,

        LocalDateTime startDate,

        LocalDateTime endDate

) {
}
