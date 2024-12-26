package com.deni.mallcoursework.infrastructure.security.expression;

import com.deni.mallcoursework.domain.user.entity.Role;
import com.deni.mallcoursework.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserExpression {
    private final BaseExpression baseExpression;
    private final UserService userService;

    @Autowired
    public UserExpression(BaseExpression baseExpression, UserService userService) {
        this.baseExpression = baseExpression;
        this.userService = userService;
    }

    public boolean canDeleteUser(String userId) {
        var authentication = baseExpression.getAuthentication();

        if (authentication == null) {
            return false;
        }

        var userById = userService.getEntityById(userId);
        var userByIdStoreId = userById.getStore().getId();

        var currentUser = baseExpression.getUser(authentication);
        var currentUserStore = currentUser.getStore();

        var isUserByIdEmployee = userById.getRole().equals(Role.EMPLOYEE);
        var isCurrentUserManager = currentUser.getRole().equals(Role.MANAGER);
        var isCurrentUserManagerOfStore = userByIdStoreId.equals(currentUserStore.getId());

        if (isUserByIdEmployee && isCurrentUserManager && isCurrentUserManagerOfStore) {
            return true;
        }

        return false;
    }

    public boolean canUpdateInfo(String userId) {
        var authentication = baseExpression.getAuthentication();

        if (authentication == null) {
            return false;
        }

        var currentUser = baseExpression.getUser(authentication);
        if (!currentUser.getId().equals(userId)) {
            return false;
        }

        return true;
    }

    public boolean canCreateUser(String role) {
        boolean isInvalidRole = Arrays.stream(Role.values()).noneMatch(r -> r.name().equals(role));

        if (isInvalidRole) {
            return false;
        }

        var authentication = baseExpression.getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (baseExpression.isAdmin(authentication)) {
            return true;
        }

        if (role.equals(Role.ADMIN.name())) {
            return false;
        }

        var currentUser = baseExpression.getUser(authentication);
        var userRole = currentUser.getRole();
        if (role.equals(Role.MALL_OWNER.name())) {
            return false;
        }

        if (role.equals(Role.MANAGER.name()) && !userRole.equals(Role.MALL_OWNER)) {
            return false;
        }

        if (role.equals(Role.EMPLOYEE.name()) && !userRole.equals(Role.MANAGER)) {
            return false;
        }

        return true;
    }
}
