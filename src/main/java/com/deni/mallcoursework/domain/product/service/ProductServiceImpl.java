package com.deni.mallcoursework.domain.product.service;

import com.cloudinary.Cloudinary;
import com.deni.mallcoursework.domain.product.dto.CreateProductDto;
import com.deni.mallcoursework.domain.product.dto.DisplayProductDto;
import com.deni.mallcoursework.domain.product.entity.Product;
import com.deni.mallcoursework.domain.product.mapper.ProductMapper;
import com.deni.mallcoursework.domain.product.repository.ProductRepository;
import com.deni.mallcoursework.domain.store.service.StoreService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    private static final String PRODUCT = "Product";
    private static final String ID = "id";
    private static final String URL = "url";

    private final ProductRepository productRepository;
    private final Cloudinary cloudinary;
    private final ProductMapper mapper;
    private final StoreService storeService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              Cloudinary cloudinary,
                              ProductMapper mapper,
                              StoreService storeService) {
        this.productRepository = productRepository;
        this.cloudinary = cloudinary;
        this.mapper = mapper;
        this.storeService = storeService;
    }

    @Override
    public void create(CreateProductDto createDto, String storeId) {
        var product = mapper.fromCreateDto(createDto);

        Map uploadResult = null;
        try {
            var file = createDto.getImageFile();
            if (!file.isEmpty()) {
                uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (uploadResult != null) {
            var url = (String) uploadResult.get(URL);
            product.setImageUrl(url);
        }

        var store = storeService.getById(storeId);
        product.setStore(store);

        productRepository.save(product);
    }

    @Override
    public Page<DisplayProductDto> getAll(Pageable pageable, String storeId) {
        return productRepository.findByStore_Id(storeId, pageable)
                .map(mapper::toDisplayProductDto);
    }

    @Override
    public DisplayProductDto getById(String id) {
        var product = getProductById(id);

        return mapper.toDisplayProductDto(product);
    }

    @Override
    public Product getEntityById(String id) {
        return getProductById(id);
    }

    @Override
    public CreateProductDto getCreateDtoById(String id) {
        var product = getProductById(id);

        return mapper.toCreateProductDto(product);
    }

    @Override
    public String update(CreateProductDto createDto, String id) {
        var product = getProductById(id);

        mapper.update(createDto, product);

        if (createDto.getImageFile().isEmpty()) {
            productRepository.save(product);
            return product.getStore().getId();
        }

        Map uploadResult;
        try {
            // get public id of cloudinary img
            var file = createDto.getImageFile();
            var imageUrl = product.getImageUrl();
            String publicId = getPublicId(imageUrl);

            // delete old image
            cloudinary.uploader().destroy(publicId, Map.of());

            // upload new image
            uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (uploadResult != null) {
            var url = (String) uploadResult.get(URL);
            product.setImageUrl(url);
        }

        productRepository.save(product);
        return product.getStore().getId();
    }

    @Override
    public String delete(String id) {
        var product = getProductById(id);

        productRepository.deleteById(id);
        try {
            var imageUrl = product.getImageUrl();
            if (StringUtils.isEmptyOrWhitespace(imageUrl)) {
                return product.getStore().getId();
            }

            cloudinary.uploader().destroy(getPublicId(imageUrl), Map.of());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return product.getStore().getId();
    }

    private Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID));
    }

    private String getPublicId(String imageUrl) {
        var lastSlash = imageUrl.lastIndexOf('/');
        var lastDot = imageUrl.lastIndexOf('.');
        var publicId = imageUrl.substring(lastSlash + 1, lastDot);

        return publicId;
    }
}
