package site.packit.packit.domain.auth.info.oauth2;

import java.util.Map;

public class KakaoOAuth2UserInfo
        extends OAuth2UserInfo {

    private final Map<String, Object> properties;
    private final Map<String, Object> kakaoAccountInfo;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        this.properties = (Map<String, Object>) attributes.get("properties");
        this.kakaoAccountInfo = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {

        if (properties == null) {
            return "";
        }

        return (String) properties.get("nickname");
    }

    @Override
    public String getEmail() {

        if (properties == null) {
            return "";
        }

        return (String) kakaoAccountInfo.get("email");
    }

    @Override
    public String getProfileImageUrl() {

        if (properties == null) {
            return "";
        }

        return (String) properties.get("thumbnail_image");
    }
}