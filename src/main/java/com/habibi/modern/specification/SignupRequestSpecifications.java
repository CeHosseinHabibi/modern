package com.habibi.modern.specification;

import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.enums.RequestStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class SignupRequestSpecifications {
    public static Specification<SignupRequest> hasStatuses(RequestStatus[] requestStatuses) {
        return (root, query, criteriaBuilder) -> {
            if (requestStatuses == null || requestStatuses.length < 1)
                return null;
            Predicate[] predicates = new Predicate[requestStatuses.length];
            for (int i = 0; i < requestStatuses.length; i++) {
                predicates[i] = criteriaBuilder.equal(root.get("requestStatus"), requestStatuses[i].ordinal());
            }
            return criteriaBuilder.or(predicates);
        };
    }

    public static Specification<SignupRequest> createdFrom(LocalDateTime createdFrom) {
        return (root, query, criteriaBuilder) -> createdFrom == null ? null :
                criteriaBuilder.greaterThanOrEqualTo(root.get("requesterEntity").get("requestedAt"), createdFrom);
    }

    public static Specification<SignupRequest> createdTo(LocalDateTime createdTo) {
        return (root, query, criteriaBuilder) -> createdTo == null ? null :
                criteriaBuilder.lessThanOrEqualTo(root.get("requesterEntity").get("requestedAt"), createdTo);
    }

    public static Specification<SignupRequest> lastRollbackTryDateIsNotNull() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("lastRollbackTryDate"));
    }

    public static Specification<SignupRequest> rollbackableSignupRequests() {
        RequestStatus[] rollbackAbleRequestStatuses = {RequestStatus.CREATED, RequestStatus.CORE_IS_UNREACHABLE};
        return Specification.where(hasStatuses(rollbackAbleRequestStatuses)).and(lastRollbackTryDateIsNotNull());
    }
}