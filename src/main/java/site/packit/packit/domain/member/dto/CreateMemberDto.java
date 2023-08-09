package site.packit.packit.domain.member.dto;

import site.packit.packit.domain.auth.constant.AuthenticationProvider;
import site.packit.packit.domain.member.entity.Member;

public record CreateMemberDto(
        String personalId,
        String email,
        String nickname,
        String profileImageUrl,
        Integer listCheckLimitCount,
        AuthenticationProvider authenticationProvider
) {
    public static CreateMemberDto of(
            String personalId,
            String email,
            String nickname,
            String profileImageUrl,
            Integer listCheckLimitCount,
            AuthenticationProvider authenticationProvider
    ) {
        return new CreateMemberDto(
                personalId,
                email,
                nickname,
                profileImageUrl,
                listCheckLimitCount,
                authenticationProvider
        );
    }

    public Member toEntity() {
        return Member.createUser(
                personalId,
                email,
                nickname,
                profileImageUrl,
                listCheckLimitCount,
                authenticationProvider
        );
    }
}