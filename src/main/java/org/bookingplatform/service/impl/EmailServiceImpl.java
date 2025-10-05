package org.bookingplatform.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookingplatform.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async("mailExecutor")
    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Password Reset Request");
            helper.setFrom("carboncreditmarketplace@gmail.com");

            // HTML template
            String content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<body>" +
                    "<h3>Password Reset Request</h3>" +
                    "<p>Click the button below to reset your password. This link will expire in 1 hour.</p>" +
                    "<a href=\"" + resetLink + "\" " +
                    "style=\"display:inline-block;padding:10px 20px;font-size:16px;" +
                    "color:white;background-color:#007bff;text-decoration:none;border-radius:5px;\">Reset Password</a>" +
                    "<p>If you didn't request a password reset, ignore this email.</p>" +
                    "</body>" +
                    "</html>";

            helper.setText(content, true); // true = HTML

            mailSender.send(message);
            log.info("Password reset email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}", to, e);
        }
    }
}