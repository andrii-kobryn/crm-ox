package com.akobryn.crm.controllers;

import com.akobryn.crm.dto.auth.AuthenticationRequest;
import com.akobryn.crm.service.AuthenticationService;
import com.akobryn.crm.service.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final CookieService cookieService;
    private final UserDetailsService userDetailsService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new AuthenticationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AuthenticationRequest registrationRequest, Model model) {
        try {
            authenticationService.register(registrationRequest);
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new AuthenticationRequest());
        return "login";
    }

    @PostMapping("/login")
    public void login(@ModelAttribute("loginRequest") AuthenticationRequest loginRequest, HttpServletResponse response) throws IOException {
        String jwt = authenticationService.login(loginRequest).getToken();
        response.addCookie(cookieService.createJwtCookie(jwt));
        response.sendRedirect("/home");
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login";
    }
}
