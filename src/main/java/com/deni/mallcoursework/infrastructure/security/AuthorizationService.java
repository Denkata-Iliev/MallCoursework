package com.deni.mallcoursework.infrastructure.security;

import com.deni.mallcoursework.domain.account.entity.Role;
import com.deni.mallcoursework.domain.account.entity.User;
import com.deni.mallcoursework.domain.account.service.UserService;
import com.deni.mallcoursework.domain.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public AuthorizationService(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    public boolean isAllowedToModifyProductId(String productId) {
        var authentication = getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (isAdmin(authentication)) {
            return true;
        }

        var product = productService.getEntityById(productId);
        var storeId = product.getStore().getId();

        if (isManagerOfStore(authentication, storeId) || isEmployeeOfStore(authentication, storeId)) {
            return true;
        }

        return false;
    }

    public boolean isAllowedToModify(String storeId) {
        var authentication = getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (isAdmin(authentication)) {
            return true;
        }

        if (isManagerOfStore(authentication, storeId) || isEmployeeOfStore(authentication, storeId)) {
            return true;
        }

        return false;
    }

    private boolean isManagerOfStore(Authentication authentication, String storeId) {
        var user = getUser(authentication);
        return user.getStore().getId().equals(storeId) && user.getRole().equals(Role.MANAGER);
    }

    private boolean isEmployeeOfStore(Authentication authentication, String storeId) {
        var user = getUser(authentication);
        return user.getStore().getId().equals(storeId) && user.getRole().equals(Role.EMPLOYEE);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private User getUser(Authentication authentication) {
        var mallUserDetails = (MallUserDetails) authentication.getPrincipal();

        return userService.getUserById(mallUserDetails.getId());
    }

    private static Authentication getAuthentication() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals("anonymousUser")) {
            return null;
        }

        return authentication;
    }
}
