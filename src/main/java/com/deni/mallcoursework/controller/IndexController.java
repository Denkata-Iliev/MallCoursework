package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.user.dto.UserFullInfoDisplayDto;
import com.deni.mallcoursework.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication == null) {
            model.addAttribute("user", new UserFullInfoDisplayDto(
                    "",
                    "Anonymous",
                    "N/A",
                    "N/A",
                    null,
                    null
            ));

            return "index";
        }

        var user = userService.getCurrentUserFullInfo(authentication);
        model.addAttribute("user", user);
        return "index";
    }
}
