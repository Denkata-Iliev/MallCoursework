package com.deni.mallcoursework.infrastructure.security.expression;

import com.deni.mallcoursework.domain.mall.entity.Mall;
import com.deni.mallcoursework.domain.mall.service.MallService;
import com.deni.mallcoursework.domain.store.entity.Store;
import com.deni.mallcoursework.domain.store.service.StoreService;
import com.deni.mallcoursework.domain.user.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreExpression {
    private final BaseExpression baseExpression;
    private final MallService mallService;
    private final StoreService storeService;

    @Autowired
    public StoreExpression(BaseExpression baseExpression, MallService mallService, StoreService storeService) {
        this.baseExpression = baseExpression;
        this.mallService = mallService;
        this.storeService = storeService;
    }

    public boolean isAllowedToCreateEmployee(String storeId) {
        var authentication = baseExpression.getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (baseExpression.isAdmin(authentication)) {
            return true;
        }

        var currentUser = baseExpression.getUser(authentication);
        var store = storeService.getById(storeId);
        var storeManager = store.getManager();
        if (!currentUser.getId().equals(storeManager.getId())) {
            return false;
        }

        return true;
    }

    public boolean isAllowedToCreateStore(String mallId) {
        var authentication = baseExpression.getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (baseExpression.isAdmin(authentication)) {
            return true;
        }

        var currentUser = baseExpression.getUser(authentication);
        var mall = mallService.getEntityById(mallId);
        var ownerId = mall.getOwner().getId();
        if (currentUser.getRole().equals(Role.MALL_OWNER) && ownerId.equals(currentUser.getId())) {
            return true;
        }

        return false;
    }

    public boolean isAllowedToUpdateStore(String storeId) {
        var authentication = baseExpression.getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (baseExpression.isAdmin(authentication)) {
            return true;
        }

        var currentUser = baseExpression.getUser(authentication);
        var currentUserStore = currentUser.getStore();
        if (currentUserStore == null) {
            return false;
        }

        if (currentUser.getRole().equals(Role.MANAGER) && currentUserStore.getId().equals(storeId)) {
            return true;
        }

        return false;
    }

    public boolean isAllowedToDeleteOrChangeManagerOfStore(String mallId, String storeId) {
        var authentication = baseExpression.getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (baseExpression.isAdmin(authentication)) {
            return true;
        }

        var currentUser = baseExpression.getUser(authentication);
        var store = storeService.getById(storeId);
        var mall = getMall(mallId, store);

        var mallOwnerId = mall.getOwner().getId();
        var mallIdOfThisStore = store.getMall().getId();

        // to delete store, user must be admin (already checked)
        // or mall owner of the mall the store is in
        boolean isUserMallOwner = currentUser.getRole().equals(Role.MALL_OWNER);
        if (!isUserMallOwner) {
            return false;
        }

        boolean isOwnerOfThisMall = mallOwnerId.equals(currentUser.getId());
        if (!isOwnerOfThisMall) {
            return false;
        }

        boolean isOwnerOfMallWhereThisStoreIs = mallIdOfThisStore.equals(mallId);
        if (mallId == null) {
            // if mallId is null, then check if the malls of the current user
            // contain a mall whose ownerId matches the ownerId of the mall we're checking against
            isOwnerOfMallWhereThisStoreIs = currentUser.getMalls()
                    .stream()
                    .anyMatch(m ->
                            m.getOwner()
                                    .getId()
                                    .equals(mallOwnerId)
                    );
        }

        if (!isOwnerOfMallWhereThisStoreIs) {
            return false;
        }

        return true;
    }

    private Mall getMall(String mallId, Store store) {
        // if mallId is null, then get the mall through the store
        if (mallId != null) {
            return mallService.getEntityById(mallId);
        }

        return store.getMall();
    }
}
