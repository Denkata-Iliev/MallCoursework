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
    private static final String USER = "User";
    private static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreService storeService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           @Lazy StoreService storeService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.storeService = storeService;
    }

    @Override
    public void register(RegisterDto registerDto) {
        var user = validateUserEncodePassword(registerDto);
        user.setRole(Role.CLIENT);

        userRepository.save(user);
    }

    @Override
    public void register(RegisterDto registerDto, String role) {
        var user = validateUserEncodePassword(registerDto);
        user.setRole(Role.valueOf(role));

        userRepository.save(user);
    }

    @Override
    public void registerEmployee(RegisterDto registerDto, String storeId) {
        var user = validateUserEncodePassword(registerDto);
        user.setRole(Role.EMPLOYEE);

        var store = storeService.getEntityById(storeId);
        user.setStore(store);

        userRepository.save(user);
    }

    @Override
    public UserDisplayDto getById(String id) {
        var user = getEntityById(id);

        return userMapper.toDisplayDto(user);
    }

    @Override
    public User getEntityById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID));
    }

    @Override
    public UserDisplayDto getCurrentUser(Authentication authentication) {
        var user = getCurrentUserEntity(authentication);

        return userMapper.toDisplayDto(user);
    }

    @Override
    public User getCurrentUserEntity(Authentication authentication) {
        var userDetails = (MallUserDetails) authentication.getPrincipal();

        return getEntityById(userDetails.getId());
    }

    @Override
    public UpdateUserDto getUpdateDto(UserDisplayDto userDisplayDto) {
        return userMapper.toUpdateDtoFromDisplayDto(userDisplayDto);
    }

    @Override
    public void update(UpdateUserDto updateUserDto, String id) {
        var user = getEntityById(id);

        var userByEmail = userRepository.findByEmail(updateUserDto.getEmail());
        if (userByEmail != null && !user.getId().equals(userByEmail.getId())) {
            throw new ConflictException(EMAIL);
        }

        var userByPhone = userRepository.findByPhone(updateUserDto.getPhone());
        if (userByPhone != null && !user.getId().equals(userByPhone.getId())) {
            throw new ConflictException(PHONE);
        }

        userMapper.update(updateUserDto, user);
        userRepository.save(user);
    }

    @Override
    public List<UserDisplayDto> getAllManagers(String id) {
        var availableManagers = userRepository.findAllByRoleAndStoreIsNull(Role.MANAGER);

        // if id is not null, add the current manager to the list,
        // so that when updating a store, it's not necessary to update the manager
        if (id != null) {
            availableManagers.add(getEntityById(id));
        }

        return availableManagers
                .stream()
                .map(userMapper::toDisplayDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDisplayDto> getAllMallOwners() {
        return userRepository.findAllByRole(Role.MALL_OWNER)
                .stream()
                .map(userMapper::toDisplayDto)
                .collect(Collectors.toList());
    }

    @Override
    public void changePassword(ChangePassDto changePassDto, String id) {
        var user = getEntityById(id);
        if (!passwordEncoder.matches(changePassDto.getOldPass(), user.getPassword())) {
            throw new PasswordMismatchException();
        }

        var encodedNewPassword = passwordEncoder.encode(changePassDto.getNewPass());
        user.setPassword(encodedNewPassword);

        userRepository.save(user);
    }

    private User validateUserEncodePassword(RegisterDto registerDto) {
        var user = userMapper.fromRegisterDto(registerDto);

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ConflictException(EMAIL);
        }

        if (userRepository.existsByPhone(registerDto.getPhone())) {
            throw new ConflictException(PHONE);
        }

        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        return user;
    }
}
