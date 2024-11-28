package com.deni.mallcoursework.domain.product.service;

import com.cloudinary.Cloudinary;
import com.deni.mallcoursework.domain.product.dto.CreateProductDto;
import com.deni.mallcoursework.domain.product.dto.DisplayProductDto;
import com.deni.mallcoursework.domain.product.mapper.ProductMapper;
import com.deni.mallcoursework.domain.product.repository.ProductRepository;
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

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, Cloudinary cloudinary, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.cloudinary = cloudinary;
        this.mapper = mapper;
    }

    @Override
    public void create(CreateProductDto createDto) {
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

        productRepository.save(product);
    }

    @Override
    public Page<DisplayProductDto> getAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(mapper::toDisplayProductDto);
    }

    @Override
    public DisplayProductDto getById(String id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID));

        return mapper.toDisplayProductDto(product);
    }

    @Override
    public CreateProductDto getCreateDtoById(String id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID));

        return mapper.toCreateProductDto(product);
    }

    @Override
    public void update(CreateProductDto createDto, String id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID));

        mapper.update(createDto, product);

        if (createDto.getImageFile().isEmpty()) {
            productRepository.save(product);
            return;
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
    }

    @Override
    public void delete(String id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID));

        productRepository.deleteById(id);
        try {
            var imageUrl = product.getImageUrl();
            if (StringUtils.isEmptyOrWhitespace(imageUrl)) {
                return;
            }

            cloudinary.uploader().destroy(getPublicId(imageUrl), Map.of());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getPublicId(String imageUrl) {
        var lastSlash = imageUrl.lastIndexOf('/');
        var lastDot = imageUrl.lastIndexOf('.');
        var publicId = imageUrl.substring(lastSlash + 1, lastDot);

        return publicId;
    }
}
