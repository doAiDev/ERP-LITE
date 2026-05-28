package com.erplite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "대시보드");
        model.addAttribute("activeMenu", "dashboard");
        return "dashboard/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
