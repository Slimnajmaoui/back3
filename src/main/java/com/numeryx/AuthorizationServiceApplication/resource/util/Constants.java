package com.numeryx.AuthorizationServiceApplication.resource.util;

public class Constants {
    public static final String HAS_ROLE_SUPER_ADMIN = "hasRole('ROLE_SUPER_ADMIN')";
    public static final String HAS_ANY_AUTHORITY = "hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN_FINANCIER','ROLE_ADMIN_PROJECT_DIRECTOR','ROLE_USER_PROVIDER','ROLE_USER_SUBSCRIBER','ROLE_ADMIN_PROVIDER','ROLE_ADMIN_SUBSCRIBER')";
    public static final String HAS_ROLE_SUPER_ADMIN_PROJECT_DIRECTOR = "hasAnyAuthority('ROLE_SUPER_ADMIN', 'ROLE_ADMIN_PROJECT_DIRECTOR')";
    public static final String HAS_ROLE_SUPER_ADMIN_PARTNER = "hasAnyAuthority('ROLE_ADMIN_PROVIDER')";

    public static final String HAS_ROLE_SUPER_ADMIN_ADMIN_SUBSCRIBER = "hasAnyAuthority('ROLE_SUPER_ADMIN', 'ROLE_ADMIN_SUBSCRIBER')";
    public static final String HAS_ROLE_ADMIN_SUBSCRIBER = "hasAnyAuthority('ROLE_ADMIN_SUBSCRIBER')";

}
