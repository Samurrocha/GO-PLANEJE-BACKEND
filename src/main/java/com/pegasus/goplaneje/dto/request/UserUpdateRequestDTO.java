package com.pegasus.goplaneje.dto.request;

import com.pegasus.goplaneje.enums.Role;
import lombok.Data;

//import javax.validation.constraints.Email;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
import javax.validation.constraints.*;

@Data
public class UserUpdateRequestDTO {

    @Email(message = "email is not valid")
    private String email;

    @Size(min = 8, max = 20, message = "password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character"
    )
    private String password;

    private Role role;

    private String username;


}
