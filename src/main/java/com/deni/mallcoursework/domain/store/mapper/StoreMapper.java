package com.deni.mallcoursework.domain.store.mapper;

import com.deni.mallcoursework.domain.user.mapper.UserMapper;
import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.dto.DetailsStoreDto;
import com.deni.mallcoursework.domain.store.dto.DisplayStoreDto;
import com.deni.mallcoursework.domain.store.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StoreMapper {
    Store fromCreateDto(CreateStoreDto createStoreDto);

    DisplayStoreDto toDisplayStoreDto(Store store);

    @Mapping(source = "store.mall.id", target = "mallId")
    DetailsStoreDto toDetailsStoreDto(Store store);

    @Mapping(source = "store.manager.id", target = "managerId")
    CreateStoreDto toCreateStoreDto(Store store);

    @Mapping(target = "id", ignore = true)
    void update(CreateStoreDto createStoreDto, @MappingTarget Store store);
}
