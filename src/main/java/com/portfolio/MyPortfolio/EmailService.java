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

    @Value("${portfolio.email.recipient:njihiavictor754@gmail.com}")
    private String recipientEmail;

    @Value("${portfolio.email.sender:noreply@victorportfolio.com}")
    private String senderEmail;

    public void sendContactMessage(ContactMessage message) {
        try {
            // Email to yourself (notification)
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(recipientEmail);
            mail.setFrom(senderEmail);
            mail.setSubject("New Contact: " + message.getName() + " - " + message.getSubject());
            mail.setText(buildNotificationText(message));
            mailSender.send(mail);

            // Auto-reply to the person
            SimpleMailMessage autoReply = new SimpleMailMessage();
            autoReply.setTo(message.getEmail());
            autoReply.setFrom(senderEmail);
            autoReply.setSubject("Thanks for reaching out, " + message.getName() + "!");
            autoReply.setText(buildAutoReplyText(message));
            mailSender.send(autoReply);

            log.info("Contact message processed for: {}", message.getEmail());
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private String buildNotificationText(ContactMessage message) {
        return String.format("""
            New message from your portfolio:
            
            Name: %s
            Email: %s
            Phone: %s
            Subject: %s
            
            Message:
            %s
            """,
                message.getName(),
                message.getEmail(),
                message.getPhone() != null ? message.getPhone() : "Not provided",
                message.getSubject() != null ? message.getSubject() : "General inquiry",
                message.getMessage()
        );
    }

    private String buildAutoReplyText(ContactMessage message) {
        return String.format("""
            Hi %s,
            
            Thanks for reaching out through my portfolio! I've received your message and will get back to you within 24 hours.
            
            Here's what you sent me:
            ------------------------
            Subject: %s
            Message: %s
            ------------------------
            
            In the meantime, feel free to:
            • Check my GitHub: hhttps://github.com/SantaVick/Micro-Services/tree/master/Services
            • Connect on LinkedIn: https://www.linkedin.com/in/victor-njihia-60618a368/
            • Follow me on Instagram: https://www.instagram.com/de__vicky/
            
            Best regards,
            Victor Njihia
            Backend Engineer
            """,
                message.getName(),
                message.getSubject() != null ? message.getSubject() : "General inquiry",
                message.getMessage()
        );
    }
}