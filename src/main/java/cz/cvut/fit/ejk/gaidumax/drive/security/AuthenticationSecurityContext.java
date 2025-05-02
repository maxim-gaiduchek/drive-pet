package cz.cvut.fit.ejk.gaidumax.drive.security;

import jakarta.ws.rs.core.SecurityContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
@Getter
public class AuthenticationSecurityContext implements SecurityContext {

    private final Authentication authentication;
    private final boolean secure;

    @Override
    public Principal getUserPrincipal() {
        return authentication.getPrincipal();
    }

    @Override
    public boolean isUserInRole(String s) {
        return authentication.getUserRole().name().equalsIgnoreCase(s);
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}
