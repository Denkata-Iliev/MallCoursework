package com.deni.mallcoursework.infrastructure.security.expression;

import com.deni.mallcoursework.domain.user.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserExpression {
    private final BaseExpression baseExpression;

    @Autowired
    public UserExpression(BaseExpression baseExpression) {
        this.baseExpression = baseExpression;
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
