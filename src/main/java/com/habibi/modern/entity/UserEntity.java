package com.habibi.modern.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDateTime;


@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class UserEntity {
    @Id
    @GeneratedValue(generator = "user-sequence-generator")
    @GenericGenerator(
            name = "user-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(unique = true)
    @Size(min = 3, max = 40, message = "The username size should be between 3 to 40.")
    @Pattern(regexp = "^(([a-z]+)|([A-Z]+))+([0-9]*)$", message = "The username must match the pattern.")
    private String username;

    @Size(min = 3, max = 40, message = "The password size should be between 3 to 40.")
    @Pattern(regexp = "^([0-9]+)([a-z]+)([A-Z]+)$", message = "The password must match the pattern.")
    private String password;

    @Size(min = 3, max = 60, message = "The first name size should be between 3 to 60.")
    private String firstName;

    @Size(min = 3, max = 60, message = "The last name size should be between 3 to 60.")
    private String lastName;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(unique = true)
    @NotBlank
    private String nationalCode;
}