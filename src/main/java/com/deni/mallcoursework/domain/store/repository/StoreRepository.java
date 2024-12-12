package com.deni.mallcoursework.domain.store.repository;

import com.deni.mallcoursework.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {
    Page<Store> findAllByMall_Id(String mallId, Pageable pageable);
}
