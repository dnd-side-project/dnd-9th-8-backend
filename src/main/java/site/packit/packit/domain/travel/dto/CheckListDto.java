package site.packit.packit.domain.travel.dto;

import java.util.List;

public record CheckListDto(
        String title,

        Integer order,

        List<ItemDto> itemDtoList
) {
}
