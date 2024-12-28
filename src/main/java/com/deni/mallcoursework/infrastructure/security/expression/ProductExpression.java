package com.deni.mallcoursework.infrastructure.security.expression;

import com.deni.mallcoursework.domain.product.service.ProductService;
import com.deni.mallcoursework.domain.user.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ProductExpression {
    private final BaseExpression baseExpression;
    private final ProductService productService;

    @Autowired
    public ProductExpression(BaseExpression baseExpression, ProductService productService) {
        this.baseExpression = baseExpression;
        this.productService = productService;
    }

    public boolean isAllowedToModifyProductsWithId(String productId) {
        var authentication = baseExpression.getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (baseExpression.isAdmin(authentication)) {
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
        var authentication = baseExpression.getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (baseExpression.isAdmin(authentication)) {
            return true;
        }

        if (isRoleOfStore(authentication, storeId, Role.MANAGER) || isRoleOfStore(authentication, storeId, Role.EMPLOYEE)) {
            return true;
        }

        return false;
    }

    private boolean isRoleOfStore(Authentication authentication, String storeId, Role role) {
        var user = baseExpression.getUser(authentication);

        if (user.getStore() == null) {
            return false;
        }

        String userStoreId = user.getStore().getId();
        return userStoreId.equals(storeId) && user.getRole().equals(role);
    }
}
