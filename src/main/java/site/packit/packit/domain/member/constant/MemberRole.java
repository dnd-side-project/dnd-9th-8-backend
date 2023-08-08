package site.packit.packit.domain.member.constant;

import lombok.Getter;

@Getter
public enum MemberRole {

    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;

    MemberRole(String key, String title) {
        this.key = key;
        this.title = title;
    }
}