package site.packit.packit.global.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {

        // TODO : 향 후 인증 기능이 추가되면 현재 인가된 사용자의 정보가 들어가도록 리팩토링
        return () -> Optional.of("tester");
    }
}