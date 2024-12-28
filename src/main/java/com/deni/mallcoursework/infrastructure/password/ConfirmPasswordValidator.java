package com.deni.mallcoursework.infrastructure.password;

import com.deni.mallcoursework.domain.user.dto.ChangePassDto;
import com.deni.mallcoursework.util.Constants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.thymeleaf.util.StringUtils;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, ChangePassDto> {

    @Override
    public boolean isValid(ChangePassDto changePassDto, ConstraintValidatorContext context) {
        var newPass = changePassDto.getNewPass();
        var confNewPass = changePassDto.getConfNewPass();

        if (StringUtils.isEmptyOrWhitespace(newPass)
                || StringUtils.isEmptyOrWhitespace(confNewPass)) {
            return false;
        }

        if (!newPass.equals(confNewPass)) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(Constants.PASSWORDS_DONT_MATCH)
                    .addPropertyNode("confNewPass")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
