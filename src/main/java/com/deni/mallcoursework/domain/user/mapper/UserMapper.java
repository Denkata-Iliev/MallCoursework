package com.deni.mallcoursework.domain.user.mapper;

import com.deni.mallcoursework.domain.user.dto.UpdateUserDto;
import com.deni.mallcoursework.domain.user.dto.UserDisplayDto;
import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromRegisterDto(RegisterDto registerDto);
    UserDisplayDto toDisplayDto(User user);
    UpdateUserDto toUpdateDtoFromDisplayDto(UserDisplayDto userDisplayDto);

    void update(UpdateUserDto updateUserDto, @MappingTarget User user);
}
