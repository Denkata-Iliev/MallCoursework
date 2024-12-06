package com.deni.mallcoursework.domain.account.service;

import com.deni.mallcoursework.domain.account.dto.ManagerDto;
import com.deni.mallcoursework.domain.account.dto.RegisterDto;
import com.deni.mallcoursework.domain.account.entity.User;

import java.util.List;

public interface UserService {
    void register(RegisterDto registerDto);

    User getUserById(String id);

    List<ManagerDto> getAllManagers();
}
