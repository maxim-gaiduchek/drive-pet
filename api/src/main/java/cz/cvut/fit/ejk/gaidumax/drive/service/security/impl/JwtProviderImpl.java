package cz.cvut.fit.ejk.gaidumax.drive.service.security.impl;

import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.security.constants.JwtClaimsConstants;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ApplicationScoped
@Slf4j
public class JwtProviderImpl implements JwtProvider {

    private final SecretKey jwtSecret;
    private final Long accessTokenAge;
    private final Long refreshTokenAge;

    public JwtProviderImpl(@ConfigProperty(name = "drive-pet.jwt.secret") String jwtSecret,
                           @ConfigProperty(name = "drive-pet.jwt.access.age") Long accessTokenAge,
                           @ConfigProperty(name = "drive-pet.jwt.refresh.age") Long refreshTokenAge) {
        this.jwtSecret = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.accessTokenAge = accessTokenAge;
        this.refreshTokenAge = refreshTokenAge;
    }

    @Override
    public Claims getAccessClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
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

    @Override
    public String generateAccessToken(User user) {
        var now = LocalDateTime.now();
        var expirationInstant = now.plusSeconds(accessTokenAge).atZone(ZoneId.systemDefault()).toInstant();
        var expiration = Date.from(expirationInstant);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setExpiration(expiration)
                .signWith(jwtSecret)
                .claim(JwtClaimsConstants.USER_ID_KEY, user.getId())
                .claim(JwtClaimsConstants.USER_ROLE_KEY, user.getRole())
                .claim(JwtClaimsConstants.CREATED_AT_KEY, now.toString())
                .claim(JwtClaimsConstants.TYPE_KEY, "ACCESS")
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        var now = LocalDateTime.now();
        var expirationInstant = now.plusSeconds(refreshTokenAge).atZone(ZoneId.systemDefault()).toInstant();
        var expiration = Date.from(expirationInstant);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setExpiration(expiration)
                .signWith(jwtSecret)
                .claim(JwtClaimsConstants.USER_ID_KEY, user.getId())
                .claim(JwtClaimsConstants.USER_ROLE_KEY, user.getRole())
                .claim(JwtClaimsConstants.CREATED_AT_KEY, now.toString())
                .claim(JwtClaimsConstants.TYPE_KEY, "REFRESH")
                .compact();
    }
}
