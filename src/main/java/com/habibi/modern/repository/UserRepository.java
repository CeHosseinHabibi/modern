package com.habibi.modern.repository;

import com.habibi.modern.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findUserEntityByUsername(String username);

    Optional<UserEntity> findUserEntityByNationalCode(String nationalCode);
}