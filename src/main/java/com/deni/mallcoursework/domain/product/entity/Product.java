package com.deni.mallcoursework.domain.product.entity;

import com.deni.mallcoursework.util.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    private String name;

    @Column(nullable = false)
    @NotNull(message = Constants.BLANK_FIELD_ERROR)
    @Positive(message = Constants.PRICE_MUST_BE_POSITIVE)
    private BigDecimal price;

    @Column(name = "img_url")
    private String imageUrl;
}
