package com.pegasus.goplaneje.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RefreshTokenResponseDTO {
    private String refreshToken;
    private String acessToken;

}
