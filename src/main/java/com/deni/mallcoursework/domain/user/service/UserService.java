package com.deni.mallcoursework.domain.user.service;

import com.deni.mallcoursework.domain.user.dto.ManagerDto;
import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.entity.User;

import java.util.List;

public interface UserService {
    void register(RegisterDto registerDto);

    User getUserById(String id);

    List<ManagerDto> getAllManagers(String id);
}
