package com.deni.mallcoursework.domain.store.service;

import com.deni.mallcoursework.domain.account.service.UserService;
import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.dto.DisplayStoreDto;
import com.deni.mallcoursework.domain.store.mapper.StoreMapper;
import com.deni.mallcoursework.domain.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final UserService userService;

    public StoreServiceImpl(StoreRepository storeRepository, StoreMapper storeMapper, UserService userService) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
        this.userService = userService;
    }

    @Override
    public void create(CreateStoreDto createStoreDto) {
        var store = storeMapper.fromCreateDto(createStoreDto);
        var manager = userService.getUserById(createStoreDto.getManagerId());

        store.setManager(manager);
        manager.setStore(store);

        storeRepository.save(store);
    }

    @Override
    public Page<DisplayStoreDto> getAll(Pageable pageable) {
        return storeRepository.findAll(pageable)
                .map(storeMapper::toDisplayStoreDto);
    }
}
