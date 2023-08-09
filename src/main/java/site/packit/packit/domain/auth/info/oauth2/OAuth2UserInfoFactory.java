package site.packit.packit.domain.auth.info.oauth2;

import site.packit.packit.domain.auth.constant.AuthenticationProvider;
import site.packit.packit.domain.auth.exception.InvalidAuthenticationProviderException;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(
            AuthenticationProvider loginProvider,
            Map<String, Object> attributes
    ) {
        return switch (loginProvider) {
            case NAVER -> new NaverOAuth2UserInfo(attributes);
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new InvalidAuthenticationProviderException();
        };
    }
}