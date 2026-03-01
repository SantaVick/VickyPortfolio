package com.portfolio.MyPortfolio;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ApiDocumentationController {

    @GetMapping("/docs")
    public String apiDocs(Model model) {
        model.addAttribute("endpoints", List.of(
                "GET /api/projects",
                "GET /api/projects/{id}",
                "GET /api/skills",
                "GET /api/experience"
        ));
        return "api-docs";
    }
}