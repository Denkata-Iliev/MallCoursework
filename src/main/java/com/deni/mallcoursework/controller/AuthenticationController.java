package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.exception.ConflictException;
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
        if (bindingResult.hasErrors()) {
            model.addAttribute("registerDto", registerDto);
            return "register";
        }

        try {
            userService.register(registerDto);
        } catch (ConflictException e) {
            bindingResult.rejectValue(e.getField(), "error.registerDto", e.getMessage());
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
}
