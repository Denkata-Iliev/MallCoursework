package com.deni.mallcoursework.domain.account.service;

import com.deni.mallcoursework.domain.account.mapper.UserMapper;
import com.deni.mallcoursework.domain.account.dto.RegisterDto;
import com.deni.mallcoursework.domain.account.entity.Role;
import com.deni.mallcoursework.domain.account.repository.UserRepository;
import com.deni.mallcoursework.infrastructure.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserRepository repository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterDto registerDto) {
        var user = userMapper.fromRegisterDto(registerDto);
        if (repository.existsByEmail(registerDto.getEmail())) {
            throw new ConflictException("email");
        }

        if (repository.existsByPhone(registerDto.getPhone())) {
            throw new ConflictException("phone");
        }

        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(Role.CLIENT);

        repository.save(user);
    }
}
