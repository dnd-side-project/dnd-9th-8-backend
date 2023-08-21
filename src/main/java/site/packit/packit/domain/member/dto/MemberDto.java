package site.packit.packit.domain.member.dto;

import site.packit.packit.domain.member.entity.Member;

public record MemberDto(
        Long memberId,
        String email,
        String nickname,
        String profileImageUrl,
        Boolean isEmailAuthorized
) {

    public static MemberDto of(
            Long memberId,
            String email,
            String nickname,
            String profileImageUrl,
            Boolean isEmailAuthorized
    ) {
        return new MemberDto(
                memberId,
                email,
                nickname,
                profileImageUrl,
                isEmailAuthorized
        );
    }

    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getProfileImageUrl(),
                member.getIsEmailAuthorized()
        );
    }
}