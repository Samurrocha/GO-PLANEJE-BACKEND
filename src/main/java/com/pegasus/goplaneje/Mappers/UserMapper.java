package com.pegasus.goplaneje.Mappers;

import com.pegasus.goplaneje.dto.request.UserUpdateRequestDTO;
import com.pegasus.goplaneje.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    // Atualiza apenas campos não nulos do DTO na entidade
    void updateUserFromDto(UserUpdateRequestDTO dto, @MappingTarget User user);
}
