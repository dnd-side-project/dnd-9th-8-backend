package site.packit.packit.domain.member.dto;

public record MemberWithTravelCountDto(
        Long memberId,
        String email,
        Boolean isEmailAuthorized,
        String nickname,
        String profileImageUrl,

        Integer travelCount
) {
    public static MemberWithTravelCountDto of(
            Long memberId,
            String email,
            Boolean isEmailAuthorized,
            String nickname,
            String profileImageUrl,
            Integer travelCount
    ) {
        return new MemberWithTravelCountDto(
                memberId,
                email,
                isEmailAuthorized,
                nickname,
                profileImageUrl,
                travelCount
        );
    }

    public static MemberWithTravelCountDto from(
            MemberDto member,
            Integer travelCount
    ) {
        return new MemberWithTravelCountDto(
                member.memberId(),
                member.email(),
                member.isEmailAuthorized(),
                member.nickname(),
                member.profileImageUrl(),
                travelCount
        );
    }
}