package com.deni.mallcoursework.domain.user.repository;

import com.deni.mallcoursework.domain.user.dto.SearchUserDto;
import com.deni.mallcoursework.domain.user.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {
    private static final String FULLNAME = "fullname";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
    private static final String ROLE = "role";

    private final SearchUserDto searchUserDto;

    public UserSpecification(SearchUserDto searchUserDto) {
        this.searchUserDto = searchUserDto;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<User> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        var andPredicates = new ArrayList<Predicate>();
        var orPredicates = new ArrayList<Predicate>();
        List<String> searchFields = searchUserDto.getFields();
        var searchText = searchUserDto.getSearchText();

        if (searchUserDto.getRole() != null) {
            andPredicates.add(criteriaBuilder.equal(root.get(ROLE), searchUserDto.getRole()));
        }

        // if there is a role and no text, we can filter by role, but
        // if there is no role specified and no text, then we can't filter
        if (StringUtils.isEmptyOrWhitespace(searchText)) {
            return criteriaBuilder.and(andPredicates.toArray(new Predicate[]{}));
        }

        searchText = "%" + searchText + "%";

        // there's text but not specified which field we should search for
        if (searchFields == null || searchFields.isEmpty()) {
            orPredicates.add(criteriaBuilder.like(root.get(FULLNAME), searchText));
            orPredicates.add(criteriaBuilder.like(root.get(EMAIL), searchText));
            orPredicates.add(criteriaBuilder.like(root.get(PHONE), searchText));

            // combine "OR" and "AND" clauses for "WHERE" statement
            Predicate orClauses = criteriaBuilder.or(orPredicates.toArray(new Predicate[]{}));
            Predicate andClauses = andPredicates.isEmpty() ? orClauses : criteriaBuilder.and(andPredicates.toArray(new Predicate[]{}));
            return query.where(orClauses, andClauses).getRestriction();
        }

        // there's text and we have specified fields to search for
        if (searchFields.contains(FULLNAME)) {
            orPredicates.add(criteriaBuilder.like(root.get(FULLNAME), searchText));
        }

        if (searchFields.contains(EMAIL)) {
            orPredicates.add(criteriaBuilder.like(root.get(EMAIL), searchText));
        }

        if (searchFields.contains(PHONE)) {
            orPredicates.add(criteriaBuilder.like(root.get(PHONE), searchText));
        }

        // combine "OR" and "AND" clauses for "WHERE" statement
        Predicate orClauses = criteriaBuilder.or(orPredicates.toArray(new Predicate[]{}));
        Predicate andClauses = andPredicates.isEmpty() ? orClauses : criteriaBuilder.and(andPredicates.toArray(new Predicate[]{}));
        return query.where(orClauses, andClauses).getRestriction();
    }
}
