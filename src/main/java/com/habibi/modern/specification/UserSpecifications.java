package com.habibi.modern.specification;

import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.enums.ContractType;
import com.habibi.modern.enums.UserRole;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class UserSpecifications {
    public static Specification<UserEntity> hasUsername(String username) {
        return (root, query, criteriaBuilder) -> username == null ? null :
                criteriaBuilder.like(root.get("username"), username);
    }

    public static Specification<UserEntity> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> firstName == null ? null :
                criteriaBuilder.like(root.get("firstName"), firstName);
    }

    public static Specification<UserEntity> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) -> lastName == null ? null :
                criteriaBuilder.like(root.get("lastName"), lastName);
    }

    public static Specification<UserEntity> createdFrom(LocalDateTime createdFrom) {
        return (root, query, criteriaBuilder) -> createdFrom == null ? null :
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdFrom);
    }

    public static Specification<UserEntity> createdTo(LocalDateTime createdTo) {
        return (root, query, criteriaBuilder) -> createdTo == null ? null :
                criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdTo);
    }

    public static Specification<UserEntity> hasNationalCode(String nationalCode) {
        return (root, query, criteriaBuilder) -> nationalCode == null ? null :
                criteriaBuilder.like(root.get("nationalCode"), nationalCode);
    }

    public static Specification<UserEntity> hasOrganizationName(String organizationName) {
        return (root, query, criteriaBuilder) -> organizationName == null ? null :
                criteriaBuilder.like(root.get("organizationName"), organizationName);
    }

    public static Specification<UserEntity> hasContractType(ContractType contractType) {
        return (root, query, criteriaBuilder) -> contractType == null ? null :
                criteriaBuilder.equal(root.get("contractType"), contractType.ordinal());
    }

    public static Specification<UserEntity> hasUserRole(UserRole userRole) {
        return (root, query, criteriaBuilder) -> userRole == null ? null :
                criteriaBuilder.equal(root.get("userRole"), userRole.ordinal());
    }

    public static Specification<UserEntity> hasCif(Long cif) {
        return (root, query, criteriaBuilder) -> cif == null ? null : criteriaBuilder.equal(root.get("cif"), cif);
    }
}