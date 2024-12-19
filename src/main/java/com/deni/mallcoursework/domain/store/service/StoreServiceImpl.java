package com.deni.mallcoursework.domain.store.service;

import com.deni.mallcoursework.domain.mall.service.MallService;
import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.dto.DetailsStoreDto;
import com.deni.mallcoursework.domain.store.dto.DisplayStoreDto;
import com.deni.mallcoursework.domain.store.entity.Store;
import com.deni.mallcoursework.domain.store.mapper.StoreMapper;
import com.deni.mallcoursework.domain.store.repository.StoreRepository;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {
    private static final String STORE = "Store";
    private static final String ID = "id";

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final UserService userService;
    private final MallService mallService;

    public StoreServiceImpl(StoreRepository storeRepository,
                            StoreMapper storeMapper,
                            UserService userService, MallService mallService) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
        this.userService = userService;
        this.mallService = mallService;
    }

    @Override
    public void create(CreateStoreDto createStoreDto, String mallId) {
        var store = storeMapper.fromCreateDto(createStoreDto);
        var manager = userService.getUserById(createStoreDto.getManagerId());
        var mall = mallService.getEntityById(mallId);

        store.setManager(manager);
        manager.setStore(store);
        store.setMall(mall);

        storeRepository.save(store);
    }

    @Override
    public Page<DisplayStoreDto> getAll(Pageable pageable, String mallId) {
        return storeRepository.findAllByMall_Id(mallId, pageable)
                .map(storeMapper::toDisplayStoreDto);
    }

    @Override
    public DetailsStoreDto getDetailsById(String id) {
        var store = getStoreById(id);
        return storeMapper.toDetailsStoreDto(store);
    }

    @Override
    public Store getById(String id) {
        return getStoreById(id);
    }

    @Override
    public DetailsStoreDto getStoreOfCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUserDisplayDto = userService.getCurrentUser(authentication);
        var currentUserEntity = userService.getUserById(currentUserDisplayDto.getId());
        var store = getStoreById(currentUserEntity.getStore().getId());

        return storeMapper.toDetailsStoreDto(store);
    }

    @Override
    public CreateStoreDto getCreateDtoById(String id) {
        var store = getStoreById(id);

        return storeMapper.toCreateStoreDto(store);
    }

    @Override
    public String update(CreateStoreDto createStoreDto, String id) {
        var store = getStoreById(id);

        storeMapper.update(createStoreDto, store);

        // since a store can't exist without a manager in the first place,
        // that means that if in the update, the managerId is null, then
        // the store has to remain with the same manager
        String newManagerId = createStoreDto.getManagerId();
        if (newManagerId == null) {
            storeRepository.save(store);
            return store.getMall().getId();
        }

        String oldManagerId = store.getManager().getId();

        // if no change in manager, just save
        if (oldManagerId.equals(newManagerId)) {
            storeRepository.save(store);
            return store.getMall().getId();
        }

        // if there is a change in managers, set the store of the old manager to null,
        // that way they'll be deleted, because a manager is directly connected to the store,
        // and it doesn't make sense to have the old manager's account connected to a different store
        // example: no sense in manager with email ivan@newyorker.com to be manager of LC Waikiki store
        var oldManager = userService.getUserById(oldManagerId);
        oldManager.setStore(null);

        var newManager = userService.getUserById(newManagerId);
        store.setManager(newManager);
        newManager.setStore(store);
        storeRepository.save(store);

        return store.getMall().getId();
    }

    @Override
    public String delete(String id) {
        var store = getStoreById(id);

        // manually detaching manager from store,
        // so the cascade deletion works correctly
        store.getManager().setStore(null);
        storeRepository.save(store);

        storeRepository.deleteById(id);

        return store.getMall().getId();
    }

    private Store getStoreById(String id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STORE, ID));
    }
}
