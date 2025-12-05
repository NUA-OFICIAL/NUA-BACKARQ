package com.nua.core.base.dto;

import com.nua.core.base.entities.NUAUserBase;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;


    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public static <T extends NUAUserBase> CustomUserDetails build(T user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}