package cz.cvut.fit.ejk.gaidumax.drive.utils;

import cz.cvut.fit.ejk.gaidumax.drive.security.JwtAuthentication;
import cz.cvut.fit.ejk.gaidumax.drive.security.constants.JwtClaimsConstants;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;
import io.jsonwebtoken.Claims;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtUtils {

    public JwtAuthentication generate(Claims claims) {
        var userId = claims.get(JwtClaimsConstants.USER_ID_KEY, Long.class);
        var role = fetchRole(claims);
        var auth = new JwtAuthentication();
        auth.setUserId(userId);
        auth.setUserRole(role);
        return auth;
    }

    private Role fetchRole(Claims claims) {
        var roleStr = claims.get(JwtClaimsConstants.USER_ROLE_KEY, String.class);
        return Role.valueOf(roleStr);
    }
}
