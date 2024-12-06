package com.deni.mallcoursework.domain.store.mapper;

import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.dto.DisplayStoreDto;
import com.deni.mallcoursework.domain.store.entity.Store;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    Store fromCreateDto(CreateStoreDto createStoreDto);

    DisplayStoreDto toDisplayStoreDto(Store store);
}
