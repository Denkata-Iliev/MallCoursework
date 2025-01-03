package com.deni.mallcoursework.domain.product.mapper;

import com.deni.mallcoursework.domain.product.dto.CreateProductDto;
import com.deni.mallcoursework.domain.product.dto.DisplayProductDto;
import com.deni.mallcoursework.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {

    Product fromCreateDto(CreateProductDto createProductDto);

    CreateProductDto toCreateProductDto(Product product);

    void update(CreateProductDto createProductDto, @MappingTarget Product product);

    @Mapping(source = "store.id", target = "storeId")
    DisplayProductDto toDisplayProductDto(Product product);
}
