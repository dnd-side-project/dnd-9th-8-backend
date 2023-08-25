package site.packit.packit.domain.auth.email.service;

public interface EmailAuthenticationService {

    void sendSimpleMessage(Long memberId) throws Exception;

    void verifyAuthenticationCode(String code);
}