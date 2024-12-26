package com.deni.mallcoursework.domain.mall.entity;

import com.deni.mallcoursework.domain.store.entity.Store;
import com.deni.mallcoursework.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Entity(name = "malls")
@Getter
@Setter
public class Mall {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @Length(min = 5, message = "Name must be at least 5 characters long!")
    private String name;

    @Column(nullable = false)
    @Length(min = 10, message = "Address must be at least 10 characters long!")
    private String address;

    @Column(nullable = false)
    @Positive(message = "Mall needs to have at least 1 floor!")
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
