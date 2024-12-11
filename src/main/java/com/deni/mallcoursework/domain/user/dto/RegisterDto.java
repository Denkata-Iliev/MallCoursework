package com.deni.mallcoursework.domain.user.dto;

import com.deni.mallcoursework.util.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class RegisterDto {
        @NotBlank(message = Constants.BLANK_FIELD_ERROR)
        @Size(min = 3, max = 20)
        private String fullname;

        @NotBlank(message = Constants.BLANK_FIELD_ERROR)
        @Size(min = 8, max = 20)
        private String password;

        @Email(message = Constants.EMAIL_ERROR)
        @Size(min = 3, max = 20)
        private String email;

        @NotBlank(message = Constants.BLANK_FIELD_ERROR)
        @Pattern(regexp = "^\\d{10}$", message = Constants.PHONE_ERROR)
        private String phone;

}
