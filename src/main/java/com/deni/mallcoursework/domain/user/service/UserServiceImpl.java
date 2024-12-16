package com.deni.mallcoursework.domain.user.service;

import com.deni.mallcoursework.domain.user.dto.UserDisplayDto;
import com.deni.mallcoursework.domain.user.entity.User;
import com.deni.mallcoursework.domain.user.mapper.UserMapper;
import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.entity.Role;
import com.deni.mallcoursework.domain.user.repository.UserRepository;
import com.deni.mallcoursework.infrastructure.exception.ConflictException;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        var user = validateUserEncodePassword(registerDto);
        user.setRole(Role.CLIENT);

        repository.save(user);
    }

    @Override
    public void register(RegisterDto registerDto, String role) {
        var user = validateUserEncodePassword(registerDto);
        user.setRole(Role.valueOf(role));

        repository.save(user);
    }

    @Override
    public User getUserById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id"));
    }

    @Override
    public List<UserDisplayDto> getAllManagers(String id) {
        var availableManagers = repository.findAllByRoleAndStoreIsNull(Role.MANAGER);

        if (id != null) {
            availableManagers.add(getUserById(id));
        }

        return availableManagers
                .stream()
                .map(userMapper::toDisplayDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDisplayDto> getAllMallOwners() {
        return repository.findAllByRole(Role.MALL_OWNER)
                .stream()
                .map(userMapper::toDisplayDto)
                .collect(Collectors.toList());
    }

    private User validateUserEncodePassword(RegisterDto registerDto) {
        var user = userMapper.fromRegisterDto(registerDto);

        if (repository.existsByEmail(registerDto.getEmail())) {
            throw new ConflictException("email");
        }

        if (repository.existsByPhone(registerDto.getPhone())) {
            throw new ConflictException("phone");
        }

        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        return user;
    }
}
