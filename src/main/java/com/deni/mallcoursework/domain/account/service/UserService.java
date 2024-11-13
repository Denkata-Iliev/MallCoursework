package com.deni.mallcoursework.domain.account.service;

import com.deni.mallcoursework.domain.account.dto.RegisterDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void register(RegisterDto registerDto);
}
