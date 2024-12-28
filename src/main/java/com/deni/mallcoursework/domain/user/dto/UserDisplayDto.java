package com.deni.mallcoursework.domain.user.dto;

import com.deni.mallcoursework.domain.user.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class UserDisplayDto {
    private String id;
    private String fullname;
    private String email;
    private String phone;
    private Role role;
}
