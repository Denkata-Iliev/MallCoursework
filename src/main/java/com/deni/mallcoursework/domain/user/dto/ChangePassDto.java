package com.deni.mallcoursework.domain.user.dto;

import com.deni.mallcoursework.infrastructure.password.ConfirmPassword;
import com.deni.mallcoursework.util.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ConfirmPassword
public final class ChangePassDto {

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Length(min = 8, message = Constants.PASSWORD_AT_LEAST_EIGHT_CHARS)
    private String oldPass;

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Length(min = 8, message = Constants.PASSWORD_AT_LEAST_EIGHT_CHARS)
    private String newPass;

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Length(min = 8, message = Constants.PASSWORD_AT_LEAST_EIGHT_CHARS)
    private String confNewPass;
}
