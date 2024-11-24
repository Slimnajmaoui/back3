package com.numeryx.AuthorizationServiceApplication.dto;

import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private RoleEnum role;
    private int attempts;
    private boolean nonLocked;
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getRole().toString());
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
