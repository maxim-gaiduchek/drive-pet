package cz.cvut.fit.ejk.gaidumax.drive.provider.security;

import cz.cvut.fit.ejk.gaidumax.drive.security.AuthenticationSecurityContext;
import cz.cvut.fit.ejk.gaidumax.drive.security.JwtAuthentication;
import cz.cvut.fit.ejk.gaidumax.drive.security.constants.SecurityContextConstants;
import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.runtime.SecurityIdentityAssociation;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtSecurityFilter implements ContainerRequestFilter {

    @Inject
    IdentityProviderManager identityProviderManager;
    @Inject
    SecurityIdentityAssociation securityIdentityAssociation;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String JWT_TYPE = "JWT";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var header = requestContext.getHeaderString(AUTHORIZATION_HEADER);
        var tokenRequest = new TokenAuthenticationRequest(new TokenCredential(header, JWT_TYPE));
        var secure = requestContext.getSecurityContext().isSecure();
        identityProviderManager.authenticate(tokenRequest).subscribe().with(
                identity -> {
                    JwtAuthentication auth = identity.getAttribute(SecurityContextConstants.AUTHENTICATION_PROPERTY);
                    var securityContext = new AuthenticationSecurityContext(auth, secure);
                    securityIdentityAssociation.setIdentity(identity);
                    requestContext.setSecurityContext(securityContext);
                });
    }
}
