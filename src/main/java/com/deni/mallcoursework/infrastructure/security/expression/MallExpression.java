package com.deni.mallcoursework.infrastructure.security.expression;

import com.deni.mallcoursework.domain.mall.service.MallService;
import com.deni.mallcoursework.domain.user.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MallExpression {
    private final BaseExpression baseExpression;
    private final MallService mallService;

    @Autowired
    public MallExpression(BaseExpression baseExpression, MallService mallService) {
        this.baseExpression = baseExpression;
        this.mallService = mallService;
    }

    public boolean isAllowedToModifyMall(String mallId) {
        var authentication = baseExpression.getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (baseExpression.isAdmin(authentication)) {
            return true;
        }

        var currentUser = baseExpression.getUser(authentication);

        if (!currentUser.getRole().equals(Role.MALL_OWNER)) {
            return false;
        }

        var mall = mallService.getEntityById(mallId);
        var ownerId = mall.getOwner().getId();
        if (!ownerId.equals(currentUser.getId())) {
            return false;
        }

        return true;
    }
}
