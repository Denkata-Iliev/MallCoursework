package com.deni.mallcoursework.domain.account.mapper;

import com.deni.mallcoursework.domain.account.dto.RegisterDto;
import com.deni.mallcoursework.domain.account.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromRegisterDto(RegisterDto registerDto);
}
