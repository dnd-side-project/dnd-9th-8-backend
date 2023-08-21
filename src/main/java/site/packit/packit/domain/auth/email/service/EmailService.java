package site.packit.packit.domain.auth.email.service;

public interface EmailService {

    void sendSimpleMessage(String to, Long memberId) throws Exception;

    void verifyAuthenticationCode(String code);
}