package com.deni.mallcoursework.domain.mall.dto;

import com.deni.mallcoursework.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class CreateMallDto {

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    private String name;

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    private String address;

    @NotNull(message = Constants.BLANK_FIELD_ERROR)
    @Positive(message = "Mall needs to have at least 1 floor")
    private Integer floors;

    private String description;

    @NotNull(message = Constants.BLANK_FIELD_ERROR)
    private String ownerId;
}
