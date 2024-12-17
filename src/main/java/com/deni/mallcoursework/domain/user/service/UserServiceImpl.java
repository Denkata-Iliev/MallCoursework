package com.deni.mallcoursework.domain.user.service;

import com.deni.mallcoursework.domain.store.service.StoreService;
import com.deni.mallcoursework.domain.user.dto.ChangePassDto;
import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.dto.UpdateUserDto;
import com.deni.mallcoursework.domain.user.dto.UserDisplayDto;
import com.deni.mallcoursework.domain.user.entity.Role;
import com.deni.mallcoursework.domain.user.entity.User;
import com.deni.mallcoursework.domain.user.mapper.UserMapper;
import com.deni.mallcoursework.domain.user.repository.UserRepository;
import com.deni.mallcoursework.infrastructure.exception.ConflictException;
import com.deni.mallcoursework.infrastructure.exception.PasswordMismatchException;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import com.deni.mallcoursework.infrastructure.security.MallUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final StoreService storeService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           UserRepository repository,
                           PasswordEncoder passwordEncoder,
                           @Lazy StoreService storeService) {
        this.userMapper = userMapper;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.storeService = storeService;
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
    public void registerEmployee(RegisterDto registerDto, String storeId) {
        var user = validateUserEncodePassword(registerDto);
        user.setRole(Role.EMPLOYEE);

        var store = storeService.getById(storeId);
        user.setStore(store);

        repository.save(user);
    }

    @Override
    public UserDisplayDto getById(String id) {
        var user = getUserById(id);

        return userMapper.toDisplayDto(user);
    }

    @Override
    public User getUserById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id"));
    }

    @Override
    public UserDisplayDto getCurrentUser(Authentication authentication) {
        var userDetails = (MallUserDetails) authentication.getPrincipal();
        var user = getUserById(userDetails.getId());

        return userMapper.toDisplayDto(user);
    }

    @Override
    public UpdateUserDto getUpdateDto(UserDisplayDto userDisplayDto) {
        return userMapper.toUpdateDtoFromDisplayDto(userDisplayDto);
    }

    @Override
    public void update(UpdateUserDto updateUserDto, String id) {
        var user = getUserById(id);

        var userByEmail = repository.findByEmail(updateUserDto.getEmail());
        if (userByEmail != null && !user.getId().equals(userByEmail.getId())) {
            throw new ConflictException("email");
        }

        var userByPhone = repository.findByPhone(updateUserDto.getPhone());
        if (userByPhone != null && !user.getId().equals(userByPhone.getId())) {
            throw new ConflictException("phone");
        }

        userMapper.update(updateUserDto, user);
        repository.save(user);
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

    @Override
    public void changePassword(ChangePassDto changePassDto, String id) {
        var user = getUserById(id);
        if (!passwordEncoder.matches(changePassDto.getOldPass(), user.getPassword())) {
            throw new PasswordMismatchException();
        }

        var encodedNewPassword = passwordEncoder.encode(changePassDto.getNewPass());
        user.setPassword(encodedNewPassword);

        repository.save(user);
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
