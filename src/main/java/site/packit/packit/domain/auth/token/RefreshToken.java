package site.packit.packit.domain.auth.token;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.packit.packit.global.audit.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_token")
@Entity
public class RefreshToken
        extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String value;

    @Column(length = 100, nullable = false, unique = true, updatable = false)
    private String memberPersonalId;

    private RefreshToken(
            String memberPersonalId,
            String value
    ) {
        this.memberPersonalId = memberPersonalId;
        this.value = value;
    }

    public static RefreshToken of(
            String memberPersonalId,
            String value
    ) {
        return new RefreshToken(memberPersonalId, value);
    }

    public void updateValue(String tokenValue) {
        this.value = tokenValue;
    }
}