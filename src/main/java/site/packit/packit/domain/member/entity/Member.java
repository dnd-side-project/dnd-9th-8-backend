package site.packit.packit.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.packit.packit.domain.auth.constant.AuthenticationProvider;
import site.packit.packit.domain.member.constant.MemberRole;
import site.packit.packit.domain.member.constant.MemberStatus;
import site.packit.packit.global.audit.BaseTimeEntity;

import static site.packit.packit.domain.member.constant.MemberRole.*;
import static site.packit.packit.domain.member.constant.MemberStatus.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
public class Member
        extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true, updatable = false)
    private String personalId;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 1000, nullable = false)
    private String profileImageUrl;

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    private Integer listCheckLimitCount;

    @Column(length = 50, nullable = false)
    private MemberStatus status = ACTIVE;

    @Column(length = 50, nullable = false)
    private MemberRole role;

    @Column(length = 50, nullable = false, updatable = false)
    private AuthenticationProvider authenticationProvider;

    private Member(
            String personalId,
            String email,
            String nickname,
            String profileImageUrl,
            Integer listCheckLimitCount,
            MemberRole role,
            AuthenticationProvider authenticationProvider
    ) {
        this.personalId = personalId;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.listCheckLimitCount = listCheckLimitCount;
        this.role = role;
        this.authenticationProvider = authenticationProvider;
    }

    public static Member createUser(
            String personalId,
            String email,
            String nickname,
            String profileImageUrl,
            Integer listCheckLimit,
            AuthenticationProvider authenticationProvider
    ) {
        return new Member(
                personalId,
                email,
                nickname,
                profileImageUrl,
                listCheckLimit,
                USER,
                authenticationProvider
        );
    }
}