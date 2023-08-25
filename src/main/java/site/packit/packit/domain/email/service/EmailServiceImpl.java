package site.packit.packit.domain.email.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import site.packit.packit.domain.email.dto.SendEmailDto;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class EmailServiceImpl
        implements EmailService {

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(SendEmailDto dto)
            throws Exception {

        log.info("보내는 대상 : {}", dto.to());

        MimeMessage message = setMessage(
                dto.title(),
                dto.content(),
                dto.to(),
                dto.from(),
                dto.senderName()
        );

        try {
            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private MimeMessage setMessage(
            String title,
            String content,
            String to,
            String from,
            String senderName
    ) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject(title);
        message.setText(content, "utf-8", "html");
        message.setFrom(new InternetAddress(from, senderName));

        return message;
    }
}