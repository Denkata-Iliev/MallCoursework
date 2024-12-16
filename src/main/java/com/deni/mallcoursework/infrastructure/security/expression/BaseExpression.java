package com.deni.mallcoursework.infrastructure.security.expression;

import com.deni.mallcoursework.domain.user.entity.User;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.security.MallUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BaseExpression {
    private final UserService userService;

    public BaseExpression(UserService userService) {
        this.userService = userService;
    }

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public User getUser(Authentication authentication) {
        var mallUserDetails = (MallUserDetails) authentication.getPrincipal();

        return userService.getUserById(mallUserDetails.getId());
    }

    public Authentication getAuthentication() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals("anonymousUser")) {
            return null;
        }

        return authentication;
    }
}
