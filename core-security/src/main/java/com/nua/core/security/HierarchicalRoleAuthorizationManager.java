package com.nua.core.security;

import com.nua.core.base.entities.enums.Roles;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

public class HierarchicalRoleAuthorizationManager extends RoleAuthorizationManager {

    private final Roles requiredRole;

    public HierarchicalRoleAuthorizationManager(Roles requiredRole) { this.requiredRole = requiredRole; }


    @Override
    protected boolean checkAuthorization(Authentication auth, RequestAuthorizationContext context) {
        return auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.replace("ROLE_", ""))
                .map(Roles::valueOf)
                .anyMatch(r -> r.level() >= requiredRole.level());
    }
}
