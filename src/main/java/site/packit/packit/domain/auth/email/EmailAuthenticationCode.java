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

    @Column(nullable = false)
    private Long memberId;

    private EmailAuthenticationCode(
            String value,
            Long memberId
    ) {
        this.value = value;
        this.memberId = memberId;
    }

    public static EmailAuthenticationCode of(
            String code,
            Long memberId
    ) {
        return new EmailAuthenticationCode(
                code,
                memberId
        );
    }
}