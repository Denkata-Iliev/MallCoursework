package com.deni.mallcoursework.domain.user.service;

import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.dto.UpdateUserDto;
import com.deni.mallcoursework.domain.user.dto.UserDisplayDto;
import com.deni.mallcoursework.domain.user.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    void register(RegisterDto registerDto);

    void register(RegisterDto registerDto, String role);

    void registerEmployee(RegisterDto registerDto, String storeId);

    UserDisplayDto getById(String id);

    User getUserById(String id);

    UserDisplayDto getCurrentUser(Authentication authentication);

    UpdateUserDto getUpdateDto(UserDisplayDto userDisplayDto);

    void update(UpdateUserDto updateUserDto, String id);

    List<UserDisplayDto> getAllManagers(String id);

    List<UserDisplayDto> getAllMallOwners();
}
