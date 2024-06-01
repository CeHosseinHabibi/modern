package com.habibi.modern.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;


@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankUser extends UserEntity {
    private Long cif = new Random().nextLong(10_000_000_000L, 100_000_000_000L);
}