package com.bappi.videoinventorymanagement.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class SecurityContextUtils {

    private static final String ANONYMOUS = "anonymous";

    private SecurityContextUtils() {
    }

    public static String getUserName() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = ANONYMOUS;

        if (null != authentication && authentication.getPrincipal() !=null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            }
            // username=authentication.getPrincipal().toString();

        } else {
            log.debug("Request not authenticated, hence no user name available");
        }

        return username;
    }

    public static Set<String> getUserRoles() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Set<String> roles = new HashSet<>();

        if (null != authentication) {
            authentication.getAuthorities().forEach(e -> roles.add(e.getAuthority()));
        }

        return roles;
    }
}
