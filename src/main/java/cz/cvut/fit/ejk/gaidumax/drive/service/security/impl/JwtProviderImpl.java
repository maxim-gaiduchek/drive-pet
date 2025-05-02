package cz.cvut.fit.ejk.gaidumax.drive.service.security.impl;

import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Slf4j
public class JwtProviderImpl implements JwtProvider {

    private final String jwtSecret;

    public JwtProviderImpl(@ConfigProperty(name = "drive-pet.jwt.access.secret") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Claims getAccessClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired {}, {}", token, expEx.getMessage());
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return null;
    }

    /*@Override
    public String generateAccessToken(User user) {
        var now = LocalDateTime.now();
        var expirationInstant = now.plusSeconds(accessTokenAge).atZone(ZoneId.systemDefault()).toInstant();
        var expiration = Date.from(expirationInstant);
        return Jwts.builder()
                .setSubject(userAuth.getId().toString())
                .setExpiration(expiration)
                .signWith(jwtAccessSecret)
                .claim(JwtClaimsConstants.USER_ID_KEY, user.getId())
                .claim(JwtClaimsConstants.USER_EMAIl_KEY, user.getEmail())
                .claim(JwtClaimsConstants.USER_ROLE_KEY, user.getRole())
                .claim(JwtClaimsConstants.CREATED_AT, now.toString())
                .claim(JwtClaimsConstants.TYPE, "ACCESS")
                .compact();
    }*/
}
