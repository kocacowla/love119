package com.smu.love119.domain.auth.dto;
import com.smu.love119.domain.user.entity.User;

import java.util.ArrayList;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
@Slf4j
public class CustomUserDetailsDTO implements UserDetails {

    private final User user;

    public CustomUserDetailsDTO(User user) {

        this.user = user;
        log.info("CustomUserDetailsDTO initialized with user: {}", user);

    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return user.getRole().toString();

            }
        });

        return collection;
    }

    @Override
    public String getPassword() {

        return user.getPassword();

    }

    @Override
    public String getUsername() {

        return user.getUsername();

    }

    public Long getId() {
        log.info("Retrieved user ID: {}", user.getId());
        return user.getId();

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

    public boolean isDeleted() {

        return user.isDeleted();

    }
}
