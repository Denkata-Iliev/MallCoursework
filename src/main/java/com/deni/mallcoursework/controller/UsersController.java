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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String create(@RequestParam String role, @RequestParam String returnUrl, RegisterDto registerDto) {
        return "users/create";
    }

    @PostMapping("/create")
    public String create(@RequestParam String role,
                         @RequestParam String returnUrl,
                         @Valid RegisterDto registerDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registerDto", registerDto);
            return "users/create";
        }

        try {
            userService.register(registerDto, role);
            return "redirect:/" + returnUrl;
        } catch (ConflictException e) {
            bindingResult.rejectValue(e.getField(), "error.registerDto", e.getMessage());
            return "users/create";
        }
    }
}
