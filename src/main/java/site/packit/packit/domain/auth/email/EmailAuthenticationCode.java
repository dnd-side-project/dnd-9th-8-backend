package site.packit.packit.domain.auth.email;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.packit.packit.global.audit.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class EmailAuthenticationCode
        extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10000, nullable = false, unique = true)
    private String value;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private Long memberId;

    private EmailAuthenticationCode(
            String value,
            String email,
            Long memberId
    ) {
        this.value = value;
        this.email = email;
        this.memberId = memberId;
    }

    public static EmailAuthenticationCode of(
            String code,
            String email,
            Long memberId
    ) {
        return new EmailAuthenticationCode(
                code,
                email,
                memberId
        );
    }
}