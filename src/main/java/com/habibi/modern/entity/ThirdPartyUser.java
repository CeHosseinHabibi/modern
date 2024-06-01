package com.habibi.modern.entity;

import com.habibi.modern.enums.ContractType;
import com.habibi.modern.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThirdPartyUser extends UserEntity {
    private String organizationName;
    private ContractType contractType;
    private UserRole userRole;
}