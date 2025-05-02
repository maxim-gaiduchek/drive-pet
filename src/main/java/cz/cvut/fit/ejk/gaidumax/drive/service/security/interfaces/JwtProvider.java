package cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces;

import io.jsonwebtoken.Claims;

public interface JwtProvider {

    Claims getAccessClaims(String token);
}
