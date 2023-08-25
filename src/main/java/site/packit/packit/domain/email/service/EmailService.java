package site.packit.packit.domain.email.service;

import site.packit.packit.domain.email.dto.SendEmailDto;

public interface EmailService {

    void sendEmail(SendEmailDto dto) throws Exception;
}