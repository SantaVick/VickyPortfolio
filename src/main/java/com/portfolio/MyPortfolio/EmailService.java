package com.portfolio.MyPortfolio;

import com.portfolio.MyPortfolio.model.ContactMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${mailgun.domain}")
    private String domain;

    @Value("${mailgun.api-key}")
    private String apiKey;

    @Value("${mailgun.sender}")
    private String sender;

    @Value("${mailgun.recipient}")
    private String recipient;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.mailgun.net/v3")
            .defaultHeader("Authorization", "")
            .build();

    public void sendContactMessage(ContactMessage message) {
        String auth = "api:" + apiKey;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        String subject = "New Contact: " + message.getName() +
                (message.getSubject() != null ? " - " + message.getSubject() : "");

        String text = String.format("Name: %s\nEmail: %s\nPhone: %s\nSubject: %s\n\nMessage:\n%s",
                message.getName(),
                message.getEmail(),
                message.getPhone() != null ? message.getPhone() : "Not provided",
                message.getSubject() != null ? message.getSubject() : "General inquiry",
                message.getMessage());

        webClient.post()
                .uri("/" + domain + "/messages")
                .header("Authorization", "Basic " + encodedAuth)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("from=" + sender +
                        "&to=" + recipient +
                        "&subject=" + subject +
                        "&text=" + text)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // block for simplicity in a small app
    }
}