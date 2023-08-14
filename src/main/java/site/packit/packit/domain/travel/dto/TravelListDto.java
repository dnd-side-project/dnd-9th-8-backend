package site.packit.packit.domain.travel.dto;

import site.packit.packit.domain.travel.constant.DestinationType;
import site.packit.packit.domain.travel.constant.TravelStatus;

import java.time.LocalDateTime;

public record TravelListDto(

        Long travelId,

        String title,

        String dDay,

        DestinationType destinationType,

        LocalDateTime startDate,

        LocalDateTime endDate


) {
}
