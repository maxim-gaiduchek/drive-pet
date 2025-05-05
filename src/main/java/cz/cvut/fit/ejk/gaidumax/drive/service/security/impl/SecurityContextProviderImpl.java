package cz.cvut.fit.ejk.gaidumax.drive.service.security.impl;

import cz.cvut.fit.ejk.gaidumax.drive.security.Authentication;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;
import cz.cvut.fit.ejk.gaidumax.drive.security.constants.SecurityContextConstants;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@Slf4j
public class SecurityContextProviderImpl implements SecurityContextProvider {

    @Inject
    SecurityIdentity identity;

    @Override
    public Authentication getAuthentication() {
        return identity.getAttribute(SecurityContextConstants.AUTHENTICATION_PROPERTY);
    }

    @Override
    public Role getUserRole() {
        return getAuthentication().getUserRole();
    }

    @Override
    public Long getUserId() {
        return getAuthentication().getUserId();
    }
}
