package com.deni.mallcoursework.domain.mall.service;

import com.deni.mallcoursework.domain.mall.dto.CreateMallDto;
import com.deni.mallcoursework.domain.mall.dto.DetailsMallDto;
import com.deni.mallcoursework.domain.mall.dto.DisplayMallDto;
import com.deni.mallcoursework.domain.mall.entity.Mall;
import com.deni.mallcoursework.domain.mall.mapper.MallMapper;
import com.deni.mallcoursework.domain.mall.repository.MallRepository;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MallServiceImpl implements MallService {
    private static final String MALL = "Mall";
    private static final String ID = "id";

    private final MallRepository mallRepository;
    private final MallMapper mallMapper;
    private final UserService userService;

    @Autowired
    public MallServiceImpl(MallRepository mallRepository, MallMapper mallMapper, UserService userService) {
        this.mallRepository = mallRepository;
        this.mallMapper = mallMapper;
        this.userService = userService;
    }

    @Override
    public void createMall(CreateMallDto createMallDto) {
        var mall = mallMapper.fromCreateDto(createMallDto);

        var mallOwner = userService.getUserById(createMallDto.getOwnerId());
        mall.setOwner(mallOwner);

        mallRepository.save(mall);
    }

    @Override
    public Page<DisplayMallDto> getAll(Pageable pageable) {
        return mallRepository.findAll(pageable)
                .map(mallMapper::toDisplayMallDto);
    }

    @Override
    public Mall getEntityById(String id) {
        return mallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MALL, ID));
    }

    @Override
    public DetailsMallDto getDetailsById(String id) {
        var mall = getEntityById(id);

        return mallMapper.toDetailsMallDto(mall);
    }

    @Override
    public CreateMallDto getCreateDtoById(String id) {
        var mall = getEntityById(id);

        return mallMapper.toCreateMallDto(mall);
    }

    @Override
    public void update(CreateMallDto createMallDto, String id) {
        var mall = getEntityById(id);

        mallMapper.update(createMallDto, mall);

        String currentOwnerId = mall.getOwner().getId();
        String newOwnerId = createMallDto.getOwnerId();
        if (!currentOwnerId.equals(newOwnerId)) {
            var owner = userService.getUserById(newOwnerId);
            mall.setOwner(owner);
        }

        mallRepository.save(mall);
    }

    @Override
    public void delete(String id) {
        if (!mallRepository.existsById(id)) {
            throw new ResourceNotFoundException(MALL, ID);
        }

        mallRepository.deleteById(id);
    }
}
