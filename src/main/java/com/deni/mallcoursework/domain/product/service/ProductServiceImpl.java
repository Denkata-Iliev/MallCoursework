package com.deni.mallcoursework.domain.product.service;

import com.cloudinary.Cloudinary;
import com.deni.mallcoursework.domain.product.dto.CreateProductDto;
import com.deni.mallcoursework.domain.product.mapper.ProductMapper;
import com.deni.mallcoursework.domain.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
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
            var url = (String) uploadResult.get("url");
            product.setImageUrl(url);
        }

        productRepository.save(product);
    }
}
