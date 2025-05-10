package cz.cvut.fit.ejk.gaidumax.drive.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Principal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtAuthentication implements Authentication {

    private Long userId;
    private Role userRole;
    private boolean authenticated;

    @Override
    public Principal getPrincipal() {
        return () -> userId.toString();
    }
}
