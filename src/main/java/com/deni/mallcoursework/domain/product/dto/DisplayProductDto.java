package com.deni.mallcoursework.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public final class DisplayProductDto {
    private String id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
}
