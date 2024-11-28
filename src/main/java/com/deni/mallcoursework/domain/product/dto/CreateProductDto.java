package com.deni.mallcoursework.domain.product.dto;

import com.deni.mallcoursework.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
public final class CreateProductDto {
    private String id;

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    private String name;

    @NotNull(message = Constants.BLANK_FIELD_ERROR)
    @Positive(message = Constants.PRICE_MUST_BE_POSITIVE)
    private BigDecimal price;

    private String description;

    private MultipartFile imageFile;
}
