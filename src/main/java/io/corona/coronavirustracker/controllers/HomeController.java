package io.corona.coronavirustracker.controllers;

import io.corona.coronavirustracker.models.LocationStats;
import io.corona.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// No Rest Controller bcz we dont want to return json format but to render the UI
@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;
    // Thymeleaf Dependency will redirect this to  home.html
    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();
        int totalReportedCases = allStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        int totalNewCases = allStats.stream().mapToInt(LocationStats::getDiffFromPrevDay).sum();
        model.addAttribute("locationStats", coronaVirusDataService.getAllStats());
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "home";
    }
}

