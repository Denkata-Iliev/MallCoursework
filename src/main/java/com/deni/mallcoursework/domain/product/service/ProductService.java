package com.deni.mallcoursework.domain.product.service;

import com.deni.mallcoursework.domain.product.dto.CreateProductDto;
import com.deni.mallcoursework.domain.product.dto.DisplayProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    void create(CreateProductDto createDto);

    Page<DisplayProductDto> getAll(Pageable pageable);

    DisplayProductDto getById(String id);

    CreateProductDto getCreateDtoById(String id);

    void update(CreateProductDto createDto, String id);

    void delete(String id);
}
