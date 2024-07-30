package com.habibi.modern.convertor;

import com.habibi.modern.dto.PaginatedResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GenericConvertor {
    public static <ReturnDtoType, SourceType> PaginatedResponse toPaginatedDtos(
            Page<SourceType> entitiesPage, Function<SourceType, ReturnDtoType> entityToDtoMapperFunction) {
        List dtos = entitiesPage.stream().map(entityToDtoMapperFunction).collect(Collectors.toList());
        PaginatedResponse response = new PaginatedResponse<>(dtos, entitiesPage.getNumber(), entitiesPage.getSize(),
                entitiesPage.hasNext(), entitiesPage.hasPrevious(), entitiesPage.getTotalElements(),
                entitiesPage.getTotalPages());
        return response;
    }
}