package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.dto.UserDto;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.UserMapper;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
public class UserController {

    @Inject
    UserService userService;
    @Inject
    UserMapper userMapper;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto get(@PathParam("id") Long id) {
        var user = userService.getByIdOrThrow(id);
        return userMapper.toDto(user);
    }
}
