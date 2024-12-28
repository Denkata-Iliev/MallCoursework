package com.deni.mallcoursework.domain.user.dto;

import com.deni.mallcoursework.domain.store.dto.DisplayStoreDto;
import com.deni.mallcoursework.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFullInfoDisplayDto {
    private String id;
    private String fullname;
    private String email;
    private String phone;
    private Role role;
    private DisplayStoreDto store;
}
