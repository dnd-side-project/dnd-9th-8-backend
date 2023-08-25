package site.packit.packit.domain.member.dto;

public record UpdateMemberRequest(
        String nickname,
        String image
) {
}