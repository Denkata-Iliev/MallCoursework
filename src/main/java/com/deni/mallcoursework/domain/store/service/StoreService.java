package com.deni.mallcoursework.domain.store.service;

import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.dto.DetailsStoreDto;
import com.deni.mallcoursework.domain.store.dto.DisplayStoreDto;
import com.deni.mallcoursework.domain.store.entity.Store;
import com.deni.mallcoursework.domain.user.dto.UserDisplayDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface StoreService {
    void create(CreateStoreDto createStoreDto, String mallId);

    Page<DisplayStoreDto> getAll(Pageable pageable, String mallId);

    Page<UserDisplayDto> getEmployeesById(Pageable pageable, String id);

    DetailsStoreDto getDetailsById(String id);

    Store getEntityById(String id);

    DetailsStoreDto getStoreOfCurrentUser(Authentication authentication);

    CreateStoreDto getCreateDtoById(String id);

    String update(CreateStoreDto createStoreDto, String id);

    String delete(String id);
}
