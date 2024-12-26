package com.deni.mallcoursework.domain.product.entity;

import com.deni.mallcoursework.domain.store.entity.Store;
import com.deni.mallcoursework.util.CloudinaryInjector;
import com.deni.mallcoursework.util.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;

@Entity(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Length(min = 5, message = Constants.FIELD_AT_LEAST_FIVE_CHARS)
    private String name;

    @Column(nullable = false)
    @NotNull(message = Constants.BLANK_FIELD_ERROR)
    @Positive(message = Constants.PRICE_MUST_BE_POSITIVE)
    private BigDecimal price;

    private String description;

    @Column(name = "img_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @PreRemove
    private void preRemove() {
        if (!StringUtils.isEmptyOrWhitespace(imageUrl)) {
            CloudinaryInjector.deleteImage(getPublicId());
        }
    }

    private String getPublicId() {
        var lastSlash = imageUrl.lastIndexOf('/');
        var lastDot = imageUrl.lastIndexOf('.');
        var publicId = imageUrl.substring(lastSlash + 1, lastDot);

        return publicId;
    }
}
