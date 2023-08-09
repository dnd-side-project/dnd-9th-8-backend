package site.packit.packit.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.packit.packit.domain.auth.token.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMemberPersonalId(String memberPersonalId);

    boolean existsByMemberPersonalId(String personalId);
}