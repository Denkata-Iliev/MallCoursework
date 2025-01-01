package com.deni.mallcoursework.domain.mall.service;

import com.deni.mallcoursework.domain.mall.dto.CreateMallDto;
import com.deni.mallcoursework.domain.mall.dto.DetailsMallDto;
import com.deni.mallcoursework.domain.mall.dto.DisplayMallDto;
import com.deni.mallcoursework.domain.mall.dto.UpdateMallDto;
import com.deni.mallcoursework.domain.mall.entity.Mall;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface MallService {
    void createMall(CreateMallDto createMallDto);

    Page<DisplayMallDto> getAll(Pageable pageable);

    Page<DisplayMallDto> getMallsOfCurrentUser(Authentication authentication, Pageable pageable);

    Mall getEntityById(String id);

    DetailsMallDto getDetailsById(String id);

    UpdateMallDto getUpdateDtoById(String id);

    void update(UpdateMallDto updateMallDto, String id);

    @Transactional
    void delete(String id);
}
