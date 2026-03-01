package com.portfolio.MyPortfolio;

import com.portfolio.MyPortfolio.model.ContactMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${portfolio.email.sender}")
    private String senderEmail;

    @Value("${portfolio.email.recipient}")
    private String recipientEmail;

    public void sendContactMessage(ContactMessage message) {

        try {
            // 1️⃣ Email to yourself
            SimpleMailMessage notification = new SimpleMailMessage();
            notification.setFrom(senderEmail);
            notification.setTo(recipientEmail);
            notification.setSubject("New Contact: " + message.getName());
            notification.setText(buildNotificationText(message));

            mailSender.send(notification);

            // 2️⃣ Auto reply
            SimpleMailMessage autoReply = new SimpleMailMessage();
            autoReply.setFrom(senderEmail);
            autoReply.setTo(message.getEmail());
            autoReply.setSubject("Thanks for reaching out, " + message.getName() + "!");
            autoReply.setText(buildAutoReplyText(message));

            mailSender.send(autoReply);

            log.info("Contact message processed successfully for: {}", message.getEmail());

        } catch (Exception e) {
            log.error("Failed to send email via SMTP: {}", e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private String buildNotificationText(ContactMessage message) {
        return """
                New message from your portfolio:

                Name: %s
                Email: %s
                Phone: %s
                Subject: %s

                Message:
                %s
                """.formatted(
                message.getName(),
                message.getEmail(),
                message.getPhone() != null ? message.getPhone() : "Not provided",
                message.getSubject() != null ? message.getSubject() : "General inquiry",
                message.getMessage()
        );
    }

    private String buildAutoReplyText(ContactMessage message) {
        return """
                Hi %s,

                Thanks for reaching out through my portfolio!
                I've received your message and will respond within 24 hours.

                Best regards,
                Victor Njihia
                Backend Engineer
                """.formatted(message.getName());
    }
}