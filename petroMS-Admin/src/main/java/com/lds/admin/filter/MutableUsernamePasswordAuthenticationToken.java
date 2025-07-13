package com.lds.admin.filter;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;


public class MutableUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String aesKey;

    public MutableUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MutableUsernamePasswordAuthenticationToken(
        Object principal,
        Object credentials,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(principal, credentials, authorities);
    }


    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }
}