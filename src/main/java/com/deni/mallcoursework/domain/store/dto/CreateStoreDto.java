package com.deni.mallcoursework.domain.store.dto;

import com.deni.mallcoursework.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public final class CreateStoreDto {
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Length(min = 5, message = "Name must be at least 5 characters long!")
    private String name;

    @NotNull(message = Constants.BLANK_FIELD_ERROR)
    private Integer floorNumber;

    @NotNull(message = Constants.BLANK_FIELD_ERROR)
    private String managerId;
}
