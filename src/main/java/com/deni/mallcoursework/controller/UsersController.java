package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.user.dto.ChangePassDto;
import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.dto.UpdateUserDto;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.exception.ConflictException;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        var userDto = userService.getCurrentUser(authentication);
        model.addAttribute("user", userDto);

        var updateUserDto = userService.getUpdateDto(userDto);
        model.addAttribute("updateUserDto", updateUserDto);

        model.addAttribute("changePassDto", new ChangePassDto());

        model.addAttribute("updateFormOpen", false);
        model.addAttribute("changePassFormOpen", false);

        return "users/profile";
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

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @Valid UpdateUserDto updateUserDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("updateUserDto", updateUserDto);
            model.addAttribute("changePassDto", new ChangePassDto());
            model.addAttribute("user", userService.getById(id));
            model.addAttribute("updateFormOpen", true);
            model.addAttribute("changePassFormOpen", false);

            return "users/profile";
        }

        try {
            userService.update(updateUserDto, id);

            return "redirect:/users/profile";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        } catch (ConflictException e) {
            bindingResult.rejectValue(e.getField(), "error.updateUserDto", e.getMessage());
            model.addAttribute("updateUserDto", updateUserDto);
            model.addAttribute("changePassDto", new ChangePassDto());
            model.addAttribute("user", userService.getById(id));
            model.addAttribute("updateFormOpen", true);
            model.addAttribute("changePassFormOpen", false);

            return "users/profile";
        }
    }
}
