package com.deni.mallcoursework.domain.product.repository;

import com.deni.mallcoursework.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByStoreId(String storeId, Pageable pageable);
}
