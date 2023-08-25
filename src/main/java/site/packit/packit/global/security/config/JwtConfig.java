package site.packit.packit.global.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.packit.packit.domain.auth.repository.RefreshTokenRepository;
import site.packit.packit.domain.auth.token.AuthTokenProvider;

@Configuration
public class JwtConfig {

    @Value("${app.jwt.secret.access-token-secret-key}")
    private String accessTokenSecretKey;
    @Value("${app.jwt.expiry.access-token-expiry}")
    private long accessTokenExpiry;
    @Value("${app.jwt.secret.refresh-token-secret-key}")
    private String refreshTokenSecretKey;
    @Value("${app.jwt.expiry.refresh-token-expiry}")
    private long refreshTokenExpiry;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtConfig(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Bean
    public AuthTokenProvider authTokenProvider() {
        return new AuthTokenProvider(
                accessTokenSecretKey,
                accessTokenExpiry,
                refreshTokenSecretKey,
                refreshTokenExpiry,
                refreshTokenRepository
        );
    }
}