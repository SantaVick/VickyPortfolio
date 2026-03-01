package com.portfolio.MyPortfolio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Slf4j
@Controller
public class HealthController {

    @GetMapping("/health")
    @ResponseBody
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "service", "victor-portfolio",
                "timestamp", String.valueOf(System.currentTimeMillis())
        );
    }
}
