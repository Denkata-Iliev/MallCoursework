package com.deni.mallcoursework.domain.store.entity;

import com.deni.mallcoursework.domain.mall.entity.Mall;
import com.deni.mallcoursework.domain.user.entity.User;
import com.deni.mallcoursework.domain.product.entity.Product;
import com.deni.mallcoursework.util.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity(name = "stores")
@Getter
@Setter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    private String name;

    @Column(nullable = false)
    @NotNull(message = Constants.BLANK_FIELD_ERROR)
    private Integer floorNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "manager_id", nullable = false, unique = true)
    private User manager;

    @ManyToOne
    @JoinColumn(name = "mall_id", nullable = false)
    private Mall mall;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "store",
            orphanRemoval = true
    )
    private Set<User> employees;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "store",
            orphanRemoval = true
    )
    private Set<Product> products;
}
