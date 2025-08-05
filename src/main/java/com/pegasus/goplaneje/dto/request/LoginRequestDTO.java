package com.pegasus.goplaneje.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
public class LoginRequestDTO {

    private String email;

    private String password;
}
