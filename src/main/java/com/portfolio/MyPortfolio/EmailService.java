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

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${portfolio.email.sender:noreply@victorportfolio.com}")
    private String senderEmail;

    @Value("${portfolio.email.recipient:njihiavictor754@gmail.com}")
    private String recipientEmail;

    public void sendContactMessage(ContactMessage message) {
        try {
            SendGrid sg = new SendGrid(sendGridApiKey);

            // 1️⃣ Email to yourself (notification)
            Mail mail = new Mail(
                    new Email(senderEmail),
                    "New Contact: " + message.getName() + " - " + (message.getSubject() != null ? message.getSubject() : "General inquiry"),
                    new Email(recipientEmail),
                    new Content("text/plain", buildNotificationText(message))
            );

            sendEmail(mail, sg);

            // 2️⃣ Auto-reply to the sender
            Mail autoReply = new Mail(
                    new Email(senderEmail),
                    "Thanks for reaching out, " + message.getName() + "!",
                    new Email(message.getEmail()),
                    new Content("text/plain", buildAutoReplyText(message))
            );

            sendEmail(autoReply, sg);

            log.info("Contact message processed successfully for: {}", message.getEmail());
        } catch (Exception e) {
            log.error("Failed to send email via SendGrid: {}", e.getMessage());
            throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
        }
    }

    private void sendEmail(Mail mail, SendGrid sg) throws Exception {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);

        if (response.getStatusCode() >= 400) {
            throw new RuntimeException("SendGrid error: " + response.getBody());
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

                Here's what you sent:
                Subject: %s
                Message: %s

                In the meantime, feel free to:
                • Check my GitHub: https://github.com/SantaVick/Micro-Services/tree/master/Services
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