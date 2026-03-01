package com.portfolio.MyPortfolio;

import com.portfolio.MyPortfolio.model.ContactMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Year;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PortfolioController {

    private final ProjectService projectService;
    private final EmailService emailService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "Victor Njihia");
        model.addAttribute("title", "Backend Engineer · Java/Spring Boot");
        model.addAttribute("experience", 2);
        model.addAttribute("location", "Nairobi, Kenya");
        model.addAttribute("featuredProjects", projectService.getFeaturedProjects());
        model.addAttribute("currentYear", Year.now().getValue());
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("name", "Victor Njihia");
        model.addAttribute("bio", "Backend engineer with 2+ years of experience building production-grade microservices. " +
                "Specialize in Java, Spring Boot, and distributed systems architecture.");
        model.addAttribute("coreSkills", List.of(
                "Java 17/21", "Spring Boot", "Spring Cloud", "Microservices",
                "PostgreSQL", "Redis", "OpenTelemetry", "Jaeger/Zipkin", "Docker",
                "Kubernetes", "Kafka/RabbitMQ", "JUnit/Mockito", "Testcontainers",
                "REST/GraphQL", "Spring Security", "JWT/OAuth2", "CI/CD", "Performance Tuning"
        ));
        model.addAttribute("currentYear", Year.now().getValue());
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("contactMessage", new ContactMessage());
        model.addAttribute("currentYear", Year.now().getValue());
        return "contact";
    }

    @PostMapping("/contact/send")
    public String sendMessage(@Valid @ModelAttribute ContactMessage contactMessage,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", "Please check your input and try again.");
            return "redirect:/contact";
        }

        try {
            emailService.sendContactMessage(contactMessage);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Thanks for reaching out! I'll get back to you within 24 hours. A confirmation has been sent to your email.");
            log.info("Contact message sent successfully from: {}", contactMessage.getEmail());
        } catch (Exception e) {
            log.error("Failed to send contact message: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Something went wrong. Please try again or email me directly at njihiavictor754@gmail.com");
        }

        return "redirect:/contact";
    }

    @GetMapping("/projects")
    public String projects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("currentYear", Year.now().getValue());
        return "projects";
    }

    @GetMapping("/projects/{id}")
    public String projectDetail(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        model.addAttribute("currentYear", Year.now().getValue());
        return "project-detail";
    }
}