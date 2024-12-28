package com.deni.mallcoursework.domain.user.dto;

import com.deni.mallcoursework.domain.user.entity.Role;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class SearchUserDto {
    private String searchText;
    private List<String> fields;
    private Role role;
}
