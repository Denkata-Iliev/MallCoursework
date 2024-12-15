package com.deni.mallcoursework.domain.mall.entity;

import com.deni.mallcoursework.domain.store.entity.Store;
import com.deni.mallcoursework.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity(name = "malls")
@Getter
@Setter
public class Mall {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer floors;

    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "mall",
            orphanRemoval = true
    )
    private Set<Store> stores;
}
