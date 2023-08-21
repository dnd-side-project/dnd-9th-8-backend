package site.packit.packit.domain.auth.email.service;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.packit.packit.domain.auth.email.EmailAuthenticationCode;
import site.packit.packit.domain.auth.email.EmailAuthenticationCodeRepository;
import site.packit.packit.domain.auth.email.exception.EmailAuthenticationException;
import site.packit.packit.domain.member.entity.Member;
import site.packit.packit.domain.member.repository.MemberRepository;
import site.packit.packit.global.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;

import static site.packit.packit.domain.auth.email.exception.EmailErrorCode.*;
import static site.packit.packit.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
public class EmailServiceImpl
        implements EmailService {

    // TODO : 도메인 & SSL 연결 후 변경
    private static final String EMAIL_AUTHENTICATION_URL = "http://localhost:8080/api/email-authentication?code=";

    private final JavaMailSender emailSender;
    private final EmailAuthenticationCodeRepository emailAuthenticationCodeRepository;
    private final MemberRepository memberRepository;

    public EmailServiceImpl(
            JavaMailSender emailSender,
            EmailAuthenticationCodeRepository emailAuthenticationCodeRepository,
            MemberRepository memberRepository
    ) {
        this.emailSender = emailSender;
        this.emailAuthenticationCodeRepository = emailAuthenticationCodeRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public void sendSimpleMessage(
            String to,
            Long memberId
    ) throws Exception {

        Member member = getMemberEntity(memberId);
        verifyEmailAuthentication(member);

        emailAuthenticationCodeRepository.deleteAllByMemberId(memberId);

        MimeMessage message = createMessage(
                to,
                generateAuthenticationLink(memberId)
        );

        try {
            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private MimeMessage createMessage(String to, String authenticationLink) throws Exception {
        log.info("보내는 대상 : {}", to);
        log.info("인증 번호 : {}", authenticationLink);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증 테스트");//제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요 양성욱입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 링크를 클릭해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 링크입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += authenticationLink + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("packit0807@gmail.com", "pack-it"));//보내는 사람

        return message;
    }

    private String generateAuthenticationLink(Long memberId) {
        StringBuffer key = new StringBuffer();
        String uuid = UUID.randomUUID().toString();
        Random rnd = new Random();
        String code;

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        code = uuid + "-" + key.toString();

        saveAuthenticationCode(code, memberId);

        return EMAIL_AUTHENTICATION_URL + code;
    }

    @Transactional
    public void saveAuthenticationCode(
            String code,
            Long memberId
    ) {
        emailAuthenticationCodeRepository.save(
                EmailAuthenticationCode.of(
                        code,
                        memberId
                )
        );
    }

    @Transactional
    @Override
    public void verifyAuthenticationCode(String authenticationCode) {

        EmailAuthenticationCode emailAuthenticationCode = emailAuthenticationCodeRepository.findByValue(authenticationCode)
                .orElseThrow(() -> new EmailAuthenticationException(INVALID_AUTHENTICATION_CODE));

        checkCodeTime(emailAuthenticationCode.getCreatedAt());

        Member member = memberRepository.getReferenceById(emailAuthenticationCode.getMemberId());
        member.emailAuthorized();

        emailAuthenticationCodeRepository.delete(emailAuthenticationCode);
    }

    private void checkCodeTime(LocalDateTime codeCreateTime) {
        long betweenMinute = ChronoUnit.SECONDS.between(codeCreateTime, LocalDateTime.now());
        if (betweenMinute > 600) {
            throw new EmailAuthenticationException(EXPIRED_AUTHENTICATION_CODE);
        }
    }

    private void verifyEmailAuthentication(Member member) {
        if (member.getIsEmailAuthorized()) {
            throw new EmailAuthenticationException(ALREADY_EMAIL_AUTHORIZED);
        }
    }

    private Member getMemberEntity(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException(MEMBER_NOT_FOUND));
    }
}