package com.deni.mallcoursework.domain.user.mapper;

import com.deni.mallcoursework.domain.user.dto.ManagerDto;
import com.deni.mallcoursework.domain.user.dto.RegisterDto;
import com.deni.mallcoursework.domain.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromRegisterDto(RegisterDto registerDto);
    ManagerDto toManagerDto(User user);
}
