package com.portfolio.MyPortfolio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private Long id;
    private String name;
    private String description;
    private String techStack;
    private String imageUrl;
    private String githubUrl;
    private String liveUrl;
    private boolean featured;
    private int completionYear;
}