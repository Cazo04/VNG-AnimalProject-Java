package com.example.logging.mapper;

import com.example.logging.dto.SysLogDto;
import com.example.logging.entity.SysLogging;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SysLoggingMapper {
    SysLoggingMapper INSTANCE = Mappers.getMapper(SysLoggingMapper.class);

    @Mapping(target = "id", ignore = true)
    SysLogging toEntity(SysLogDto dto);
    SysLogDto toDto(SysLogging entity);
}
