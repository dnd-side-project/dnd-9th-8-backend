package site.packit.packit.domain.auth.service;

import jakarta.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.packit.packit.domain.auth.constant.AuthenticationProvider;
import site.packit.packit.domain.auth.constant.LoginMemberStatus;
import site.packit.packit.domain.auth.exception.OAuth2ProviderMisMatchException;
import site.packit.packit.domain.auth.info.CustomUserPrincipal;
import site.packit.packit.domain.auth.info.oauth2.OAuth2UserInfo;
import site.packit.packit.domain.auth.info.oauth2.OAuth2UserInfoFactory;
import site.packit.packit.domain.member.dto.CreateMemberDto;
import site.packit.packit.domain.member.entity.Member;
import site.packit.packit.domain.member.repository.MemberRepository;
import site.packit.packit.domain.member.service.MemberService;
import site.packit.packit.global.exception.ResourceNotFoundException;

import java.util.Collection;
import java.util.List;

import static site.packit.packit.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
public class CustomOAuth2UserService
        extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ServletRequest request;

    @Value("${app.default-profile-image}")
    private String DEFAULT_PROFILE_IMAGE_URL;

    public CustomOAuth2UserService(
            MemberRepository memberRepository,
            MemberService memberService,
            ServletRequest request
    ) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.request = request;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return this.process(userRequest, oAuth2User);
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 메인 로직
     */
    private OAuth2User process(
            OAuth2UserRequest userRequest,
            OAuth2User oAuth2User
    ) {
        AuthenticationProvider authenticationProvider = AuthenticationProvider.valueOf(
                userRequest
                        .getClientRegistration()
                        .getRegistrationId()
                        .toUpperCase()
        );

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                authenticationProvider,
                oAuth2User.getAttributes()
        );

        request.setAttribute("memberStatus", LoginMemberStatus.EXISTING_MEMBER);

        Member member = memberRepository.findByPersonalId(oAuth2UserInfo.getId())
                .orElseGet(() -> register(oAuth2UserInfo, authenticationProvider));
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole().toString()));

        if (authenticationProvider != member.getAuthenticationProvider()) {
            throw new OAuth2ProviderMisMatchException(member.getAuthenticationProvider().toString());
        }

        return CustomUserPrincipal.from(
                member,
                authorities,
                oAuth2User.getAttributes()
        );
    }

    private Member register(
            OAuth2UserInfo userInfo,
            AuthenticationProvider authenticationProvider
    ) {
        Long createdMemberId = memberService.createMember(
                CreateMemberDto.of(
                        userInfo.getId(),
                        userInfo.getEmail(),
                        userInfo.getName(),
                        DEFAULT_PROFILE_IMAGE_URL,
                        authenticationProvider
                )
        );

        request.setAttribute("memberStatus", LoginMemberStatus.NEW_MEMBER);

        return memberRepository.findById(createdMemberId)
                .orElseThrow(() -> new ResourceNotFoundException(MEMBER_NOT_FOUND));
    }
}