package com.deni.mallcoursework.domain.user.dto;

import com.deni.mallcoursework.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ChangePassDto {

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Size(min = 8, max = 20)
    private String oldPass;

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Size(min = 8, max = 20)
    private String newPass;

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Size(min = 8, max = 20)
    private String confNewPass;
}
