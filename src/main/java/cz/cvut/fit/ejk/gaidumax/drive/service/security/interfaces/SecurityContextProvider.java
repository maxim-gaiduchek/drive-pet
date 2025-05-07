package cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.security.Authentication;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;

import java.util.Optional;

public interface SecurityContextProvider {

    Optional<Authentication> getAuthenticationOptional();

    Authentication getAuthentication();

    Role getUserRole();

    Long getUserId();
}
