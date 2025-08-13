package com.pegasus.goplaneje.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
  
}


