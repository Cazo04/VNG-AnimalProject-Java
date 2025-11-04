package com.example.logging.mapper;

import com.example.logging.dto.ApiLogDto;
import com.example.logging.entity.ApiLogging;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ApiLoggingMapper {
    ApiLoggingMapper INSTANCE = Mappers.getMapper(ApiLoggingMapper.class);

    @Mapping(target = "requestId", expression = "java(mapRequestId(dto.getRequestId()))")
    ApiLogging toEntity(ApiLogDto dto);
    
    @Mapping(target = "requestId", expression = "java(entity.getRequestId() != null ? entity.getRequestId().toString() : null)")
    ApiLogDto toDto(ApiLogging entity);
    
    default UUID mapRequestId(String requestId) {
        if (requestId == null || requestId.trim().isEmpty()) {
            return UUID.randomUUID();
        }
        try {
            return UUID.fromString(requestId);
        } catch (IllegalArgumentException e) {
            return UUID.randomUUID();
        }
    }
}
