package cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtProvider {

    Claims getAccessClaims(String token);

    String generateAccessToken(User user);

    String generateRefreshToken(User user);
}
