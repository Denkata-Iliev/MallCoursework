package com.deni.mallcoursework.domain.store.dto;

import com.deni.mallcoursework.domain.account.dto.ManagerDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DetailsStoreDto {
    private String id;
    private String name;
    private Integer floorNumber;
    private ManagerDto manager;
}
