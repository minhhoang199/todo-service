package com.example.toDo.model.enums;

import lombok.Getter;

@Getter
public enum ERole {
    ROLE_USER(2),
    ROLE_ADMIN(1);

    public int id;

    ERole(int id) {
        this.id = id;
    }

    public static ERole of(int id) {
        switch (id) {
            case 2:
                return ROLE_USER;
            case 1:
                return ROLE_ADMIN;
            default:
                throw new IllegalArgumentException("Unknown Role: " + id);
        }
    }
}
