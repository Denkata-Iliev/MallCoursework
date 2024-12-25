package com.deni.mallcoursework.domain.user.service;

import com.deni.mallcoursework.domain.store.dto.DisplayStoreDto;
import com.deni.mallcoursework.domain.user.dto.*;
import com.deni.mallcoursework.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    Page<UserDisplayDto> getAll(Pageable pageable, SearchUserDto searchUserDto);

    void delete(String id);

    void register(RegisterDto registerDto);

    void register(RegisterDto registerDto, String role);

    void registerEmployee(RegisterDto registerDto, String storeId);

    UserDisplayDto getById(String id);

    User getEntityById(String id);

    UserDisplayDto getCurrentUser(Authentication authentication);

    User getCurrentUserEntity(Authentication authentication);

    UpdateUserDto getUpdateDto(UserDisplayDto userDisplayDto);

    void update(UpdateUserDto updateUserDto, String id);

    List<UserDisplayDto> getAllManagers(String id);

    List<UserDisplayDto> getAllMallOwners();

    void changePassword(ChangePassDto changePassDto, String id);

    void addFavorite(String storeId, Authentication authentication);

    void removeFavorite(String storeId, Authentication authentication);

    Page<DisplayStoreDto> getCurrentUserFavorites(Authentication authentication, Pageable pageable);
}
