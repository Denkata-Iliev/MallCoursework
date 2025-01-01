package com.deni.mallcoursework.domain.mall.dto;

import com.deni.mallcoursework.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UpdateMallDto {

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Length(min = 5, message = Constants.FIELD_AT_LEAST_FIVE_CHARS)
    private String name;

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Length(min = 10, message = "Address must be at least 10 characters long!")
    private String address;

    @NotNull(message = Constants.BLANK_FIELD_ERROR)
    @Positive(message = "Mall needs to have at least 1 floor!")
    private Integer floors;

    private String description;

    private String ownerId;
}
