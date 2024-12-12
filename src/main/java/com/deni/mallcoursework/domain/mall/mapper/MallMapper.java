package com.deni.mallcoursework.domain.mall.mapper;

import com.deni.mallcoursework.domain.mall.dto.CreateMallDto;
import com.deni.mallcoursework.domain.mall.dto.DisplayMallDto;
import com.deni.mallcoursework.domain.mall.entity.Mall;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MallMapper {

    Mall fromCreateDto(CreateMallDto createMallDto);

    @Mapping(target = "storeCount", expression = "java(mall.getStores() != null ? mall.getStores().size() : 0)")
    DisplayMallDto toDisplayMallDto(Mall mall);
}
