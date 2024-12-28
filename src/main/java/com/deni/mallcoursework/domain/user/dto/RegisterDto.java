package com.deni.mallcoursework.domain.user.dto;

import com.deni.mallcoursework.util.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public final class RegisterDto {
        @NotBlank(message = Constants.BLANK_FIELD_ERROR)
        @Length(min = 5, message = Constants.FIELD_AT_LEAST_FIVE_CHARS)
        private String fullname;

        @NotBlank(message = Constants.BLANK_FIELD_ERROR)
        @Length(min = 8, message = Constants.PASSWORD_AT_LEAST_EIGHT_CHARS)
        private String password;

        @Email(message = Constants.EMAIL_ERROR)
        @Length(min = 5, message = Constants.FIELD_AT_LEAST_FIVE_CHARS)
        private String email;

        @NotBlank(message = Constants.BLANK_FIELD_ERROR)
        @Pattern(regexp = "^\\d{10}$", message = Constants.PHONE_ERROR)
        private String phone;

}
