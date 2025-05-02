package cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.security.Authentication;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;

public interface SecurityContextProvider {
    Authentication getAuthentication();

    Role getUserRole();

    Long getUserId();
}
