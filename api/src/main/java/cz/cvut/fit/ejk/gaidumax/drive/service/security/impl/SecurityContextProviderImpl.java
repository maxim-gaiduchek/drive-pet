package cz.cvut.fit.ejk.gaidumax.drive.service.security.impl;

import cz.cvut.fit.ejk.gaidumax.drive.security.Authentication;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;
import cz.cvut.fit.ejk.gaidumax.drive.security.constants.SecurityContextConstants;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequestScoped
@Slf4j
public class SecurityContextProviderImpl implements SecurityContextProvider {

    @Inject
    SecurityIdentity identity;

    @Override
    public Optional<Authentication> getAuthenticationOptional() {
        return Optional.ofNullable(getAuthentication());
    }

    @Override
    public Authentication getAuthentication() {
        return identity.getAttribute(SecurityContextConstants.AUTHENTICATION_PROPERTY);
    }

    @Override
    public Role getUserRole() {
        return getAuthenticationOptional()
                .map(Authentication::getUserRole)
                .orElse(null);
    }

    @Override
    public Long getUserId() {
        return getAuthenticationOptional()
                .map(Authentication::getUserId)
                .orElse(null);
    }
}
