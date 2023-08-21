package site.packit.packit.domain.auth.email;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthenticationCodeRepository
        extends JpaRepository<EmailAuthenticationCode, Long> {

    Optional<EmailAuthenticationCode> findByValue(String value);

    void deleteAllByMemberId(Long memberId);
}