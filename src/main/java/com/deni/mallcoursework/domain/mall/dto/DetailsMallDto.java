package com.deni.mallcoursework.domain.mall.dto;

import com.deni.mallcoursework.domain.user.dto.UserDisplayDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DetailsMallDto {
    private String id;
    private String name;
    private String address;
    private Integer floors;
    private String description;
    private UserDisplayDto owner;
}
