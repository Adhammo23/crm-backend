package com.adham.crm_backend.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {
    public static UserDetails getCurrentUser(){
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     if (authentication == null)
         throw new IllegalStateException("No authenticated user found");

     if (!(authentication.getPrincipal() instanceof UserDetails)){
         throw new IllegalStateException("Authenticated principal is not UserDetails");}

    return (UserDetails) authentication.getPrincipal();
    }
}
