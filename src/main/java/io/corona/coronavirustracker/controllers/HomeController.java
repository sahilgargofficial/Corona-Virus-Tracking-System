package io.corona.coronavirustracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// No Rest Controller bcz we dont want to return json format but to render the UI
@Controller
public class HomeController {

    // Thymeleaf Dependency will redirect this to  home.html
    @GetMapping("/")
    public String home() {
        return "home";
    }
}

