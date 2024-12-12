package com.deni.mallcoursework.domain.mall.service;

import com.deni.mallcoursework.domain.mall.dto.CreateMallDto;
import com.deni.mallcoursework.domain.mall.dto.DisplayMallDto;
import com.deni.mallcoursework.domain.mall.entity.Mall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MallService {
    void createMall(CreateMallDto createMallDto);
    Page<DisplayMallDto> getAll(Pageable pageable);
    Mall getEntityById(String id);
}
