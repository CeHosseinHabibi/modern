package com.habibi.modern.entity;

import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"REQUESTED_AT", "USER_NATIONAL_CODE"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    @Id
    @GeneratedValue(generator = "signup-request-sequence-generator")
    @GenericGenerator(
            name = "signup-request-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "signup_request_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    private RequestStatus requestStatus;
    private ErrorCode lastRollbackTryErrorCode;
    private String lastRollbackTryDescription;
    private Date lastRollbackTryDate;
    @Embedded
    private RequesterEntity requesterEntity;
}