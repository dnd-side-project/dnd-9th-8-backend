package site.packit.packit.domain.travel.dto;

public record ItemDto(
        String title,

        Integer order,

        Boolean isChecked
) {
}
