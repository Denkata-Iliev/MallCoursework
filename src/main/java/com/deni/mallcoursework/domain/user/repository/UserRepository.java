package com.deni.mallcoursework.domain.user.repository;

import com.deni.mallcoursework.domain.user.entity.Role;
import com.deni.mallcoursework.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    User findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    List<User> findAllByRoleAndStoreIsNull(Role role);
    List<User> findAllByRole(Role role);
}
