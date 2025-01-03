package com.deni.mallcoursework.domain.mall.mapper;

import com.deni.mallcoursework.domain.mall.dto.CreateMallDto;
import com.deni.mallcoursework.domain.mall.dto.DetailsMallDto;
import com.deni.mallcoursework.domain.mall.dto.DisplayMallDto;
import com.deni.mallcoursework.domain.mall.dto.UpdateMallDto;
import com.deni.mallcoursework.domain.mall.entity.Mall;
import com.deni.mallcoursework.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MallMapper {

    Mall fromCreateDto(CreateMallDto createMallDto);

    @Mapping(target = "storeCount", expression = "java(mall.getStores() != null ? mall.getStores().size() : 0)")
    DisplayMallDto toDisplayMallDto(Mall mall);

    DetailsMallDto toDetailsMallDto(Mall mall);

    void update(UpdateMallDto updateMallDto, @MappingTarget Mall mall);

    @Mapping(source = "owner.id", target = "ownerId")
    UpdateMallDto toUpdateMallDto(Mall mall);
}
