package com.bdbl.rms.controller;

import com.bdbl.rms.service.DashboardService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class WebController {

    private final DashboardService dashboardService;

    @Autowired
    public WebController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("metrics", dashboardService.getDashboardMetrics());
        return "dashboard";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }
}
