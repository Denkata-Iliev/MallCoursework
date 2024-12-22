package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.user.dto.ChangePassDto;
import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.dto.UpdateUserDto;
import com.deni.mallcoursework.domain.user.dto.UserDisplayDto;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.exception.ConflictException;
import com.deni.mallcoursework.infrastructure.exception.PasswordMismatchException;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        var userDisplayDto = userService.getCurrentUser(authentication);
        var updateUserDto = userService.getUpdateDto(userDisplayDto);

        addToModel(model,
                updateUserDto,
                userDisplayDto,
                new ChangePassDto(),
                false,
                false
        );

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

    @PreAuthorize("@userExpression.canUpdateInfo(#id)")
    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @Valid UpdateUserDto updateUserDto,
                         BindingResult bindingResult,
                         Model model) {
        var userDisplayDto = userService.getById(id);
        if (bindingResult.hasErrors()) {
            addToModel(model,
                    updateUserDto,
                    userDisplayDto,
                    new ChangePassDto(),
                    true,
                    false
            );

            return "users/profile";
        }

        try {
            userService.update(updateUserDto, id);

            return "redirect:/users/profile";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        } catch (ConflictException e) {
            bindingResult.rejectValue(e.getField(), "error.updateUserDto", e.getMessage());
            addToModel(model,
                    updateUserDto,
                    userDisplayDto,
                    new ChangePassDto(),
                    true,
                    false
            );

            return "users/profile";
        }
    }

    @PreAuthorize("@userExpression.canUpdateInfo(#id)")
    @PostMapping("/change-password/{id}")
    public String changePassword(@PathVariable String id,
                                 @Valid ChangePassDto changePassDto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            var userDisplayDto = userService.getById(id);
            var updateUserDto = userService.getUpdateDto(userDisplayDto);
            addToModel(model,
                    updateUserDto,
                    userDisplayDto,
                    changePassDto,
                    false,
                    true
            );

            return "users/profile";
        }

        try {
            userService.changePassword(changePassDto, id);

            return "redirect:/users/profile";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        } catch (PasswordMismatchException e) {
            bindingResult.rejectValue("oldPass", "error.changePassDto", e.getMessage());

            var userDisplayDto = userService.getById(id);
            var updateUserDto = userService.getUpdateDto(userDisplayDto);
            addToModel(model,
                    updateUserDto,
                    userDisplayDto,
                    changePassDto,
                    false,
                    true
            );

            return "users/profile";
        }
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping("/favorites/{storeId}")
    public ResponseEntity<String> addFavorite(@PathVariable String storeId, Authentication authentication) {
        try {
            userService.addFavorite(storeId, authentication);

            return ResponseEntity.ok("Store added to favorites!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @DeleteMapping("/favorites/{storeId}")
    public ResponseEntity<String> removeFavorite(@PathVariable String storeId, Authentication authentication) {
        try {
            userService.removeFavorite(storeId, authentication);

            return ResponseEntity.ok("Store removed from favorites!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void addToModel(Model model,
                            UpdateUserDto updateUserDto,
                            UserDisplayDto userDisplayDto,
                            ChangePassDto changePassDto,
                            boolean updateFormOpen,
                            boolean changePassFormOpen) {
        model.addAttribute("updateUserDto", updateUserDto);
        model.addAttribute("user", userDisplayDto);
        model.addAttribute("changePassDto", changePassDto);
        model.addAttribute("updateFormOpen", updateFormOpen);
        model.addAttribute("changePassFormOpen", changePassFormOpen);
    }
}
