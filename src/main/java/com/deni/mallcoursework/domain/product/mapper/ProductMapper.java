package com.deni.mallcoursework.domain.product.mapper;

import com.deni.mallcoursework.domain.product.dto.CreateProductDto;
import com.deni.mallcoursework.domain.product.dto.DisplayProductDto;
import com.deni.mallcoursework.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product fromCreateDto(CreateProductDto createProductDto);

    CreateProductDto toCreateProductDto(Product product);

    void update(CreateProductDto createProductDto, @MappingTarget Product product);

    DisplayProductDto toDisplayProductDto(Product product);
}
