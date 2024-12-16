package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.exception.ConflictException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("@userExpression.canCreateUser(#role)")
    @GetMapping("/create")
    public String create(@RequestParam String role,
                         @RequestParam String returnUrl,
                         @RequestParam(required = false) String storeId,
                         RegisterDto registerDto) {
        return "users/create";
    }

    @PreAuthorize("@userExpression.canCreateUser(#role)")
    @PostMapping("/create")
    public String create(@RequestParam String role,
                         @RequestParam String returnUrl,
                         @RequestParam(required = false) String storeId,
                         @Valid RegisterDto registerDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registerDto", registerDto);
            return "users/create";
        }

        try {
            if (!StringUtils.isEmptyOrWhitespace(storeId)) {
                userService.registerEmployee(registerDto, storeId);
                return "redirect:/" + returnUrl;
            }

            userService.register(registerDto, role);
            return "redirect:/" + returnUrl;
        } catch (ConflictException e) {
            bindingResult.rejectValue(e.getField(), "error.registerDto", e.getMessage());
            return "users/create";
        }
    }
}
