package cz.cvut.fit.ejk.gaidumax.drive.provider.security;

import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@ApplicationScoped
@Slf4j
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String JWT_TYPE = "JWT";

    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, IdentityProviderManager identityProviderManager) {
        var header = context.request().getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(header)) {
            return Uni.createFrom().nullItem();
        }
        var tokenRequest = new TokenAuthenticationRequest(new TokenCredential(header, JWT_TYPE));
        return identityProviderManager.authenticate(tokenRequest);
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        return Uni.createFrom().optional(Optional.empty());
    }
}
