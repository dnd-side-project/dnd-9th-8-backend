package site.packit.packit.domain.auth.info;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import site.packit.packit.domain.auth.constant.AuthenticationProvider;
import site.packit.packit.domain.member.constant.MemberStatus;
import site.packit.packit.domain.member.entity.Member;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static site.packit.packit.domain.member.constant.MemberStatus.ACTIVE;

@Getter
public class CustomUserPrincipal
        implements UserDetails, OAuth2User, OidcUser {
    private final Long memberId;

    private final String personalId;

    private final AuthenticationProvider authenticationProvider;

    private final MemberStatus memberStatus;

    private Collection<? extends GrantedAuthority> authorities;

    private final Map<String, Object> oAuth2UserInfoAttributes;

    private CustomUserPrincipal(
            final Long memberId,
            final String personalId,
            final AuthenticationProvider authenticationProvider,
            final MemberStatus memberStatus,
            final Collection<? extends GrantedAuthority> authorities,
            final Map<String, Object> oAuth2UserInfoAttributes
    ) {
        this.memberId = memberId;
        this.personalId = personalId;
        this.authenticationProvider = authenticationProvider;
        this.memberStatus = memberStatus;
        this.authorities = authorities;
        this.oAuth2UserInfoAttributes = oAuth2UserInfoAttributes;
    }

    public static CustomUserPrincipal from(
            final Member member,
            final Collection<? extends GrantedAuthority> authorities
    ) {
        return from(member, authorities, null);
    }

    public static CustomUserPrincipal from(
            final Member member,
            final Collection<? extends GrantedAuthority> authorities,
            final Map<String, Object> oAuth2UserInfo
    ) {
        return new CustomUserPrincipal(
                member.getId(),
                member.getPersonalId(),
                member.getAuthenticationProvider(),
                member.getStatus(),
                authorities,
                oAuth2UserInfo
        );
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfoAttributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getName() {
        return this.personalId;
    }

    @Override
    public String getUsername() {
        return this.personalId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.memberStatus == ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.memberStatus == ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.memberStatus == ACTIVE;
    }

    @Override
    public boolean isEnabled() {
        return this.memberStatus == ACTIVE;
    }

    @Override
    public Map<String, Object> getClaims() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append(" [");
        sb.append("personalId=").append(this.personalId).append(", ");
        sb.append("providerType=").append(this.authenticationProvider).append(", ");
        sb.append("memberStatus=").append(this.memberStatus).append(", ");
        sb.append("Granted Authorities=").append(this.authorities).append("], ");
        sb.append("oAuth2UserInfoAttributes=[PROTECTED]");
        return sb.toString();
    }
}