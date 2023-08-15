package site.packit.packit.domain.auth.service;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.packit.packit.domain.auth.constant.AuthenticationProvider;
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

    // TODO : 기획이 결정되면 기본 값 자동 주입으로 리팩토링
    private String memberDefaultProfileImageUrl = "DEFAULT_PROFILE_IMAGE_URL";
    private String memberDefaultNickname = "여행자";
    private Integer memberDefaultListCheckLimitCount = 2;

    public CustomOAuth2UserService(
            MemberRepository memberRepository,
            MemberService memberService
    ) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
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

        // 최초 로그인일 경우 회원 등록
        Member member = memberRepository.findByPersonalId(oAuth2UserInfo.getId())
                .orElseGet(() -> register(oAuth2UserInfo, authenticationProvider));
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole().toString()));

        // 회원 가입 된 계정의 로그인 유형과 현재 로그인 한 유형이 일치한지 검증
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
                        userInfo.getProfileImageUrl(),
                        memberDefaultListCheckLimitCount,
                        authenticationProvider
                )
        );

        return memberRepository.findById(createdMemberId)
                .orElseThrow(() -> new ResourceNotFoundException(MEMBER_NOT_FOUND));
    }
}