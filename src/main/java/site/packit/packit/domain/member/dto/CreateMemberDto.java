package site.packit.packit.domain.member.dto;

import site.packit.packit.domain.auth.constant.AuthenticationProvider;
import site.packit.packit.domain.member.entity.Member;

public record CreateMemberDto(
        String personalId,
        String email,
        String nickname,
        String profileImageUrl,
        AuthenticationProvider authenticationProvider
) {
    public static CreateMemberDto of(
            String personalId,
            String email,
            String nickname,
            String profileImageUrl,
            AuthenticationProvider authenticationProvider
    ) {
        return new CreateMemberDto(
                personalId,
                email,
                nickname,
                profileImageUrl,
                authenticationProvider
        );
    }

    public Member toEntity() {
        return Member.createUser(
                personalId,
                email,
                nickname,
                profileImageUrl,
                authenticationProvider
        );
    }
}