package com.deni.mallcoursework.domain.account.repository;

import com.deni.mallcoursework.domain.account.entity.Role;
import com.deni.mallcoursework.domain.account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    List<User> findAllByRoleAndStoreIsNull(Role role);
}
