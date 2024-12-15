package com.deni.mallcoursework.domain.user.entity;

import com.deni.mallcoursework.domain.mall.entity.Mall;
import com.deni.mallcoursework.domain.store.entity.Store;
import com.deni.mallcoursework.util.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    private String fullname;

    @Column(nullable = false)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    private String password;

    @Column(nullable = false, unique = true)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Email(message = Constants.EMAIL_ERROR)
    @Size(min = 3)
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = Constants.BLANK_FIELD_ERROR)
    @Size(min = 10, max = 10, message = Constants.PHONE_ERROR)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "owner",
            orphanRemoval = true
    )
    private Set<Mall> malls;
}
