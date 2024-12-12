package com.deni.mallcoursework.domain.mall.repository;

import com.deni.mallcoursework.domain.mall.entity.Mall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MallRepository extends JpaRepository<Mall, String> {
}
