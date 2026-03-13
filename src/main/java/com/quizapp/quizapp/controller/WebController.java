package com.quizapp.quizapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.quizapp.quizapp.model.User;
import com.quizapp.quizapp.repo.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    private final UserRepository userRepo;

    public WebController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user) {
        userRepo.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {

        User user = userRepo.findByEmail(email);

        if(user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);

            if (user.getRole().toString().equals("STUDENT"))
                return "redirect:/student/dashboard";
            else
                return "redirect:/teacher/dashboard";
        }

        return "login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

