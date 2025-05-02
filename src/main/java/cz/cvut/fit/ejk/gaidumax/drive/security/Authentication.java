package cz.cvut.fit.ejk.gaidumax.drive.security;

import java.security.Principal;

public interface Authentication {

    Long getUserId();

    Role getUserRole();

    boolean isAuthenticated();

    Principal getPrincipal();
}
