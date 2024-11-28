package com.deni.mallcoursework.domain.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;

@Getter
@Setter
public final class DisplayProductDto {
    private String id;
    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;

    public String getShortDescription() {
        if (description == null || StringUtils.isEmptyOrWhitespace(description)) {
            return "No description available";
        }

        return description.length() > 50 ? description.substring(0, 50) + "..." : description;
    }
}
