package com.deni.mallcoursework.domain.store.service;

import com.deni.mallcoursework.domain.mall.service.MallService;
import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.dto.DetailsStoreDto;
import com.deni.mallcoursework.domain.store.dto.DisplayStoreDto;
import com.deni.mallcoursework.domain.store.entity.Store;
import com.deni.mallcoursework.domain.store.mapper.StoreMapper;
import com.deni.mallcoursework.domain.store.repository.StoreRepository;
import com.deni.mallcoursework.domain.user.dto.UserDisplayDto;
import com.deni.mallcoursework.domain.user.entity.Role;
import com.deni.mallcoursework.domain.user.mapper.UserMapper;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {
    private static final String STORE = "Store";
    private static final String ID = "id";

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final UserService userService;
    private final MallService mallService;
    private final UserMapper userMapper;

    public StoreServiceImpl(StoreRepository storeRepository,
                            StoreMapper storeMapper,
                            UserService userService,
                            MallService mallService,
                            UserMapper userMapper) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
        this.userService = userService;
        this.mallService = mallService;
        this.userMapper = userMapper;
    }

    @Override
    public void create(CreateStoreDto createStoreDto, String mallId) {
        var store = storeMapper.fromCreateDto(createStoreDto);
        var manager = userService.getEntityById(createStoreDto.getManagerId());
        var mall = mallService.getEntityById(mallId);

        store.setManager(manager);
        manager.setStore(store);
        store.setMall(mall);

        storeRepository.save(store);
    }

    @Override
    public Page<DisplayStoreDto> getAll(Pageable pageable, String mallId) {
        return storeRepository.findAllByMallId(mallId, pageable)
                .map(storeMapper::toDisplayStoreDto);
    }

    @Override
    public Page<UserDisplayDto> getEmployeesById(Pageable pageable, String id) {
        var store = getEntityById(id);

        // because a manager is just a user with the role manager,
        // and an employee is just a user with the role employee,
        // the set of users that represents the employees contains the manager of the store,
        // therefore we need to filter the manager out before passing the employees of the store
        List<UserDisplayDto> employees = store.getEmployees()
                .stream()
                .filter(employee -> !employee.getRole().equals(Role.MANAGER))
                .map(userMapper::toDisplayDto)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), employees.size());

        List<UserDisplayDto> paginatedList = employees.subList(start, end);
        return new PageImpl<>(paginatedList, pageable, employees.size());
    }

    @Override
    public DetailsStoreDto getDetailsById(String id) {
        var store = getEntityById(id);

        return storeMapper.toDetailsStoreDto(store);
    }

    @Override
    public Store getEntityById(String id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STORE, ID));
    }

    @Override
    public DetailsStoreDto getStoreOfCurrentUser(Authentication authentication) {
        var currentUser = userService.getCurrentUserEntity(authentication);
        var store = getEntityById(currentUser.getStore().getId());

        return storeMapper.toDetailsStoreDto(store);
    }

    @Override
    public CreateStoreDto getCreateDtoById(String id) {
        var store = getEntityById(id);

        return storeMapper.toCreateStoreDto(store);
    }

    @Override
    public String update(CreateStoreDto createStoreDto, String id) {
        var store = getEntityById(id);

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
        var oldManager = userService.getEntityById(oldManagerId);
        oldManager.setStore(null);

        var newManager = userService.getEntityById(newManagerId);
        store.setManager(newManager);
        newManager.setStore(store);
        storeRepository.save(store);

        return store.getMall().getId();
    }

    @Override
    public String delete(String id) {
        var store = getEntityById(id);

        // manually detaching manager from store,
        // so the cascade deletion works correctly
        store.getManager().setStore(null);
        storeRepository.save(store);

        storeRepository.deleteById(id);

        return store.getMall().getId();
    }
}
