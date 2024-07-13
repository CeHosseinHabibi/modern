package com.habibi.modern.util;

import com.habibi.modern.dto.RequesterDto;
import com.habibi.modern.entity.RequesterEntity;

public class Utils {

    public static RequesterEntity getRequesterEntity(RequesterDto requesterDto) {
        return new RequesterEntity(requesterDto.getRequestedAt(), requesterDto.getUserNationalCode());
    }

    public static RequesterDto getRequesterDto(RequesterEntity requesterEntity) {
        return requesterEntity == null ?
                null : new RequesterDto(requesterEntity.getRequestedAt(), requesterEntity.getUserNationalCode());
    }
}