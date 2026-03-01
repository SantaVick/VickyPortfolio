package com.portfolio.MyPortfolio;

import com.portfolio.MyPortfolio.model.ContactMessage;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${portfolio.email.recipient:njihiavictor754@gmail.com}")
    private String recipientEmail;

    @Value("${portfolio.email.sender:noreply@victorportfolio.com}")
    private String senderEmail;

    public void sendContactMessage(ContactMessage message) {
        try {
            Email from = new Email(senderEmail);
            Email to = new Email(recipientEmail);

            String subject = "New Contact: " + message.getName() + " - " +
                    (message.getSubject() != null ? message.getSubject() : "General inquiry");

            Content content = new Content("text/plain", buildNotificationText(message));
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 400) {
                log.error("Failed to send email via SendGrid: {}", response.getBody());
                throw new RuntimeException("Email sending failed: " + response.getBody());
            }

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
}