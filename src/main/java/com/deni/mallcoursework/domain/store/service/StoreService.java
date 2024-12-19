package com.deni.mallcoursework.domain.store.service;

import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.dto.DetailsStoreDto;
import com.deni.mallcoursework.domain.store.dto.DisplayStoreDto;
import com.deni.mallcoursework.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreService {
    void create(CreateStoreDto createStoreDto, String mallId);

    Page<DisplayStoreDto> getAll(Pageable pageable, String mallId);

    DetailsStoreDto getDetailsById(String id);

    Store getById(String id);

    DetailsStoreDto getStoreOfCurrentUser();

    CreateStoreDto getCreateDtoById(String id);

    String update(CreateStoreDto createStoreDto, String id);

    String delete(String id);
}
