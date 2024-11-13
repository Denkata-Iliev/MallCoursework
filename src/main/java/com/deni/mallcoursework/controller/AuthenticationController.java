package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.account.dto.RegisterDto;
import com.deni.mallcoursework.domain.account.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(RegisterDto registerDto) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterDto registerDto, BindingResult bindingResult, Model model) {

        try {
            userService.register(registerDto);
        } catch (RuntimeException e) {
            bindingResult.rejectValue("email", "error.registerDto", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("registerDto", registerDto);
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Invalid username or password!");
        }
        return "login";
    }

    @GetMapping("/special")
    public String special() {
        return "special";
    }
}
