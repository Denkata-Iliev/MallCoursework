package com.deni.mallcoursework.domain.store.service;

import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.dto.DetailsStoreDto;
import com.deni.mallcoursework.domain.store.dto.DisplayStoreDto;
import com.deni.mallcoursework.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreService {
    void create(CreateStoreDto createStoreDto);

    Page<DisplayStoreDto> getAll(Pageable pageable);

    DetailsStoreDto getDetailsById(String id);

    Store getById(String id);

    CreateStoreDto getCreateDtoById(String id);

    void update(CreateStoreDto createStoreDto, String id);

    void delete(String id);
}
