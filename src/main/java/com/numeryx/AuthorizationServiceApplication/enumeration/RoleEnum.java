package com.numeryx.AuthorizationServiceApplication.enumeration;

import java.util.Arrays;

public enum RoleEnum {
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    ROLE_ADMIN_FINANCIER("ROLE_ADMIN_FINANCIER"),
    ROLE_ADMIN_PROJECT_DIRECTOR("ROLE_ADMIN_PROJECT_DIRECTOR"),
    ROLE_USER_PROVIDER("ROLE_USER_PROVIDER"),
    ROLE_USER_SUBSCRIBER("ROLE_USER_SUBSCRIBER"),
    ROLE_ADMIN_PROVIDER("ROLE_ADMIN_PROVIDER"),
    ROLE_ADMIN_SUBSCRIBER("ROLE_ADMIN_SUBSCRIBER");

    String type;

    private RoleEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static RoleEnum getEnum(String value) {
        for (RoleEnum v : values())
            if (v.getType().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException("Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
    }

}
