package com.deni.mallcoursework.domain.product.mapper;

import com.deni.mallcoursework.domain.product.dto.CreateProductDto;
import com.deni.mallcoursework.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "imageFile", target = "imgUrl", ignore = true)
    Product fromCreateDto(CreateProductDto createProductDto);
}
