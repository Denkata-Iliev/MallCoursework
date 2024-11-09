package com.deni.mallcoursework.domain.account.entity;

import com.deni.mallcoursework.util.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    private String fullname;

    @Column(nullable = false)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    private String password;

    @Column(nullable = false, unique = true)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Email(message = Constants.EMAIL_ERROR)
    @Size(min = 3)
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Size(min = 10, max = 10, message = Constants.PHONE_LENGTH)
    private String phone;
}
