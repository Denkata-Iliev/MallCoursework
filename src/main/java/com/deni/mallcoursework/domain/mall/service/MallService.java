package com.deni.mallcoursework.domain.mall.service;

import com.deni.mallcoursework.domain.mall.dto.CreateMallDto;
import com.deni.mallcoursework.domain.mall.dto.DetailsMallDto;
import com.deni.mallcoursework.domain.mall.dto.DisplayMallDto;
import com.deni.mallcoursework.domain.mall.entity.Mall;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MallService {
    void createMall(CreateMallDto createMallDto);

    Page<DisplayMallDto> getAll(Pageable pageable);

    Mall getEntityById(String id);

    DetailsMallDto getDetailsById(String id);

    CreateMallDto getCreateDtoById(String id);

    void update(CreateMallDto createMallDto, String id);

    @Transactional
    void delete(String id);
}
