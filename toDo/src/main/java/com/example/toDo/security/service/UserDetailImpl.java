package com.example.toDo.security.service;

import com.example.toDo.model.User;
import com.example.toDo.model.enums.ERole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailImpl implements UserDetails {
    private final String username;
    @JsonIgnore
    private final String password;
    private final GrantedAuthority authorities;

    public UserDetailImpl(String username, String password, GrantedAuthority authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetails build(User user) {
        return new UserDetailImpl(
                user.getUsername(),
                user.getPassword(),
                new SimpleGrantedAuthority(ERole.of(user.getRole().getRole().getId()).name())); // new SimpleGrantedAuthority(String)
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(this.authorities);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
