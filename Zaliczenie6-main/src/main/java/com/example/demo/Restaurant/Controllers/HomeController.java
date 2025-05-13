package com.example.demo.Restaurant.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"/", "/index"})
    public String home() {
        return "index";
    }
    @GetMapping("/index2")
    public String index2(HttpSession session, Model model) {
        // Add session ID to the model
        if (session != null) {
            model.addAttribute("sessionId", session.getId());
        }

        // Add user information if needed
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }

        return "index2";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }
    @GetMapping("settings.html#profile")
    public String profile() {
        return "profile";
    }

}
