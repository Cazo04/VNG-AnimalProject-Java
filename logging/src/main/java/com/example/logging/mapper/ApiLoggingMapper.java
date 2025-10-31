package com.example.logging.mapper;

import com.example.logging.dto.ApiLogDto;
import com.example.logging.entity.ApiLogging;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApiLoggingMapper {
    ApiLoggingMapper INSTANCE = Mappers.getMapper(ApiLoggingMapper.class);

    ApiLogging toEntity(ApiLogDto dto);
    ApiLogDto toDto(ApiLogging entity);
}
