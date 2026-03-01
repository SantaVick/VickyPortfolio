package com.portfolio.MyPortfolio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Service
public class ProjectService {

    private final List<Project> projects = new ArrayList<>();

    public ProjectService() {
        // Featured Projects (3 shown on homepage)
        projects.addAll(Arrays.asList(
                Project.builder()
                        .id(1L)
                        .name("Payment Orchestrator")
                        .description("High-throughput payment processing system handling 10k+ transactions daily. Implements idempotency keys, distributed tracing with OpenTelemetry, and circuit breakers for resilience.")
                        .techStack("Java 21, Spring Boot, Redis, Kafka, PostgreSQL, Jaeger")
                        .featured(true)
                        .completionYear(2024)
                        .githubUrl("github.com/victornjihia/payment-orchestrator")
                        .build(),

                Project.builder()
                        .id(2L)
                        .name("E-Commerce Platform")
                        .description("Scalable microservices architecture for product catalog, inventory, and order management. Features Redis caching, rate limiting, and Kubernetes orchestration.")
                        .techStack("Spring Cloud, Kubernetes, Redis, PostgreSQL, Docker")
                        .featured(true)
                        .completionYear(2024)
                        .githubUrl("github.com/victornjihia/ecommerce-platform")
                        .build(),

                Project.builder()
                        .id(3L)
                        .name("Real-Time Analytics Pipeline")
                        .description("Event-driven system processing millions of events/day using Kafka and Spring Cloud Stream. Instrumented with OpenTelemetry for end-to-end tracing.")
                        .techStack("Kafka, Spring Cloud Stream, OpenTelemetry, Grafana, Jaeger")
                        .featured(true)
                        .completionYear(2023)
                        .githubUrl("github.com/victornjihia/analytics-pipeline")
                        .build(),

                // Additional projects (shown on projects page)
                Project.builder()
                        .id(4L)
                        .name("Authentication Service")
                        .description("Centralized OAuth2 authorization server with JWT tokens, supporting multiple client applications. Includes session management with Redis.")
                        .techStack("Spring Security, OAuth2, JWT, Redis, PostgreSQL")
                        .featured(false)
                        .completionYear(2023)
                        .githubUrl("github.com/victornjihia/auth-service")
                        .build(),

                Project.builder()
                        .id(5L)
                        .name("Inventory Management System")
                        .description("Real-time inventory tracking with Redis caching and WebSocket updates for live stock changes.")
                        .techStack("Spring Boot, WebSocket, Redis, PostgreSQL, React")
                        .featured(false)
                        .completionYear(2024)
                        .githubUrl("github.com/victornjihia/inventory-system")
                        .build(),

                Project.builder()
                        .id(6L)
                        .name("URL Shortener")
                        .description("High-performance URL shortening service with Redis caching, rate limiting, and analytics tracking.")
                        .techStack("Spring Boot, Redis, PostgreSQL, Docker, Prometheus")
                        .featured(false)
                        .completionYear(2023)
                        .githubUrl("github.com/victornjihia/url-shortener")
                        .build()
        ));

        log.info("ProjectService initialized with {} projects", projects.size());
    }

    public List<Project> getAllProjects() {
        return projects;
    }

    public List<Project> getFeaturedProjects() {
        return projects.stream()
                .filter(Project::isFeatured)
                .toList();
    }

    public Project getProjectById(Long id) {
        return projects.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}