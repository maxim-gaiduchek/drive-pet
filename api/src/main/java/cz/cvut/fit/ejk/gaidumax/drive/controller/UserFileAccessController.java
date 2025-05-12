package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.dto.UserAccessDto;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.UserFileAccessMapper;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.AuthService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;
import java.util.UUID;

@Path("/")
public class UserFileAccessController {

    @Inject
    FileService fileService;
    @Inject
    UserFileAccessMapper userFileAccessMapper;
    @Inject
    AuthService authService;

    @GET
    @Path("/files/{fileId}/accesses")
    @Authenticated
    public List<UserAccessDto> getAllByFile(@PathParam("fileId") UUID id) {
        authService.checkUserIsOwnerOfFile(id);
        var file = fileService.getByIdOrThrow(id);
        return userFileAccessMapper.toDtos(file.getAccesses());
    }

    @POST
    @Path("/files/{fileId}/accesses/users/{userId}")
    @Authenticated
    public UserAccessDto create(@PathParam("fileId") UUID fileId, @PathParam("userId") Long userId,
                                @Valid UserAccessDto userAccessDto) {
        authService.checkUserIsOwnerOfFile(fileId);
        var access = fileService.createAccess(fileId, userId, userAccessDto);
        return userFileAccessMapper.toDto(access);
    }

    @PUT
    @Path("/files/{fileId}/accesses/users/{userId}")
    @Authenticated
    public UserAccessDto update(@PathParam("fileId") UUID fileId, @PathParam("userId") Long userId,
                                @Valid UserAccessDto userAccessDto) {
        authService.checkUserIsOwnerOfFile(fileId);
        var access = fileService.updateAccess(fileId, userId, userAccessDto);
        return userFileAccessMapper.toDto(access);
    }

    @DELETE
    @Path("/files/{fileId}/accesses/users/{userId}")
    @Authenticated
    public void delete(@PathParam("fileId") UUID fileId, @PathParam("userId") Long userId) {
        authService.checkUserIsOwnerOfFile(fileId);
        fileService.deleteAccess(fileId, userId);
    }
}
