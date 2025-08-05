package com.pegasus.goplaneje.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    COMMON(0),
    ADMIN(1);

    private final int code;

    public int getCode() {
        return code;
    }

    public static Role fromCode(int code){

        for (Role role : Role.values()){
            if (role.getCode() == code) return role;
        }
        throw new IllegalArgumentException("Role not found");
    }

}
