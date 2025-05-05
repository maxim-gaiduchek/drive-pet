package cz.cvut.fit.ejk.gaidumax.drive.provider.security;

import cz.cvut.fit.ejk.gaidumax.drive.exception.AccessException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.AccessExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.security.constants.SecurityContextConstants;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.JwtProvider;
import cz.cvut.fit.ejk.gaidumax.drive.utils.JwtUtils;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JwtIdentityProvider implements IdentityProvider<TokenAuthenticationRequest> {

    private static final String BEARER_PREFIX = "Bearer ";

    @Inject
    JwtProvider jwtProvider;

    @Override
    public Uni<SecurityIdentity> authenticate(TokenAuthenticationRequest authenticationRequest,
                                              AuthenticationRequestContext authenticationRequestContext) {
        var header = authenticationRequest.getToken().getToken();
        if (header == null) {
            return Uni.createFrom().nullItem();
        }
        if (!header.startsWith(BEARER_PREFIX)) {
            return Uni.createFrom().failure(new AccessException(AccessExceptionCode.TOKEN_INVALID));
        }
        var token = fetchToken(header);
        var claims = jwtProvider.getAccessClaims(token);
        if (claims == null) {
            return Uni.createFrom().failure(new AccessException(AccessExceptionCode.TOKEN_INVALID));
        }
        var auth = JwtUtils.generate(claims);
        auth.setAuthenticated(true);
        var identity = QuarkusSecurityIdentity.builder()
                .setPrincipal(auth.getPrincipal())
                .addRole(auth.getUserRole().name())
                .addAttribute(SecurityContextConstants.AUTHENTICATION_PROPERTY, auth)
                .build();
        return Uni.createFrom().item(identity);
    }

    private String fetchToken(String header) {
        return header.substring(BEARER_PREFIX.length());
    }

    @Override
    public Class<TokenAuthenticationRequest> getRequestType() {
        return TokenAuthenticationRequest.class;
    }
}
