package site.packit.packit.domain.auth.email.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.packit.packit.domain.auth.dto.EmailAuthenticationCodeRequest;
import site.packit.packit.domain.auth.email.service.EmailAuthenticationService;
import site.packit.packit.domain.auth.info.CustomUserPrincipal;
import site.packit.packit.global.response.success.SuccessApiResponse;

@RestController
public class EmailController {

    private final EmailAuthenticationService emailAuthenticationService;

    public EmailController(EmailAuthenticationService emailAuthenticationService) {
        this.emailAuthenticationService = emailAuthenticationService;
    }

    @PostMapping("/api/authentication-email")
    public ResponseEntity<SuccessApiResponse> getEmailAuthenticationCode(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) throws Exception {
        emailAuthenticationService.sendSimpleMessage(principal.getMemberId());

        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "성공적으로 인증 메일이 발송되었습니다."
                )
        );
    }

    @PostMapping("/api/email-authentication")
    public ResponseEntity<SuccessApiResponse> authenticationCode(
            @RequestBody EmailAuthenticationCodeRequest request
    ) {

        emailAuthenticationService.verifyAuthenticationCode(request.code());

        return ResponseEntity.ok(
                SuccessApiResponse.of("성공적으로 이메일이 인증되었습니다.")
        );
    }
}