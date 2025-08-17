package com.pegasus.goplaneje.dto.response;

import com.pegasus.goplaneje.enums.Role;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserUpdateResponseDTO {
    private String id;
    private String username;
    private String email;
    private Role role;
}
