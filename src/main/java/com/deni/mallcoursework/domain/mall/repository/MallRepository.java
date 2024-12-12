package com.deni.mallcoursework.domain.mall.repository;

import com.deni.mallcoursework.domain.mall.entity.Mall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MallRepository extends JpaRepository<Mall, String> {
    @Modifying
    @Query("UPDATE users u SET u.store = null WHERE u.store.mall.id = :mallId")
    void detachManagersByMallId(String mallId);
}
