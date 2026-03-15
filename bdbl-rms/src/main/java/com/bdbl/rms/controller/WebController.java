package com.bdbl.rms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/tenants")
    public String tenants() {
        return "tenants";
    }

    @GetMapping("/agreements")
    public String agreements() {
        return "agreements";
    }

    @GetMapping("/billing")
    public String billing() {
        return "billing";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    @GetMapping("/buildings")
    public String buildings() {
        return "buildings";
    }

    @GetMapping("/payments")
    public String payments() {
        return "payments";
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }
}
