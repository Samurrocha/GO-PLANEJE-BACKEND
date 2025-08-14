package com.pegasus.goplaneje.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class UserResponseDTO {
    private String id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
