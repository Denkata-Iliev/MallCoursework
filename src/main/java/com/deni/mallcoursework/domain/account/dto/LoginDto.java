package com.deni.mallcoursework.domain.account.dto;

import com.deni.mallcoursework.util.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class LoginDto {
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Email(message = Constants.EMAIL_ERROR)
    private String email;

    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    private String password;
}
