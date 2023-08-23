package site.packit.packit.domain.auth.dto;

public record EmailAuthenticationCodeRequest(
        String code
) {
}