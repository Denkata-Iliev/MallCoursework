package com.deni.mallcoursework.domain.user.service;

import com.deni.mallcoursework.domain.user.dto.UserDisplayDto;
import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.entity.User;

import java.util.List;

public interface UserService {
    void register(RegisterDto registerDto);

    void register(RegisterDto registerDto, String role);

    void registerEmployee(RegisterDto registerDto, String storeId);

    User getUserById(String id);

    List<UserDisplayDto> getAllManagers(String id);

    List<UserDisplayDto> getAllMallOwners();
}
