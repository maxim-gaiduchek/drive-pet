package cz.cvut.fit.ejk.gaidumax.drive.controller.security;

import cz.cvut.fit.ejk.gaidumax.drive.dto.security.JwtResponseDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.security.LoginDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.security.RegistrationDto;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/security")
public class AuthController {

    @Inject
    UserService userService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public JwtResponseDto login(LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public JwtResponseDto register(RegistrationDto registrationDto) {
        return userService.register(registrationDto);
    }
}
