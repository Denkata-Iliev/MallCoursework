package com.deni.mallcoursework.infrastructure.security;

import com.deni.mallcoursework.domain.user.entity.Role;
import com.deni.mallcoursework.domain.user.entity.User;
import com.deni.mallcoursework.domain.user.service.UserService;
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

    public boolean isAllowedToModifyProductsWithId(String productId) {
        var authentication = getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (isAdmin(authentication)) {
            return true;
        }

        var product = productService.getEntityById(productId);
        var storeId = product.getStore().getId();

        if (isRoleOfStore(authentication, storeId, Role.MANAGER) || isRoleOfStore(authentication, storeId, Role.EMPLOYEE)) {
            return true;
        }

        return false;
    }

    public boolean isAllowedToModifyProductsWithStoreId(String storeId) {
        var authentication = getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (isAdmin(authentication)) {
            return true;
        }

        if (isRoleOfStore(authentication, storeId, Role.MANAGER) || isRoleOfStore(authentication, storeId, Role.EMPLOYEE)) {
            return true;
        }

        return false;
    }

    private boolean isRoleOfStore(Authentication authentication, String storeId, Role role) {
        var user = getUser(authentication);

        if (user.getStore() == null) {
            return false;
        }

        String userStoreId = user.getStore().getId();
        return userStoreId.equals(storeId) && user.getRole().equals(role);
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
