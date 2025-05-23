package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FileDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UserAccessDto;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.FileMapper;
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

@Path("/files")
public class UserFileAccessController {

    @Inject
    FileService fileService;
    @Inject
    UserFileAccessMapper userFileAccessMapper;
    @Inject
    AuthService authService;
    @Inject
    FileMapper fileMapper;

    @GET
    @Path("/{fileId}/accesses")
    @Authenticated
    public List<UserAccessDto> getAllByFile(@PathParam("fileId") UUID id) {
        authService.checkUserIsOwnerOfFile(id);
        var accesses = fileService.getAllAccessesByFileId(id);
        return userFileAccessMapper.toDtos(accesses);
    }

    @POST
    @Path("/{fileId}/accesses/users/{userId}")
    @Authenticated
    public UserAccessDto create(@PathParam("fileId") UUID fileId, @PathParam("userId") Long userId,
                                @Valid UserAccessDto userAccessDto) {
        authService.checkUserIsOwnerOfFile(fileId);
        var access = fileService.createAccess(fileId, userId, userAccessDto);
        return userFileAccessMapper.toDto(access);
    }

    @POST
    @Path("/{fileId}/accesses")
    @Authenticated
    public FileDto createAccessByAccessToken(@PathParam("fileId") UUID id) {
        var file = fileService.createAccessToken(id);
        return fileMapper.toDto(file);
    }

    @PUT
    @Path("/{fileId}/accesses/users/{userId}")
    @Authenticated
    public UserAccessDto update(@PathParam("fileId") UUID fileId, @PathParam("userId") Long userId,
                                @Valid UserAccessDto userAccessDto) {
        authService.checkUserIsOwnerOfFile(fileId);
        var access = fileService.updateAccess(fileId, userId, userAccessDto);
        return userFileAccessMapper.toDto(access);
    }

    @PUT
    @Path("/accesses/{token}")
    @Authenticated
    public FileDto createAccessByAccessToken(@PathParam("token") String accessToken) {
        var file = fileService.addAccessByAccessToken(accessToken);
        return fileMapper.toDto(file);
    }

    @DELETE
    @Path("/{fileId}/accesses/users/{userId}")
    @Authenticated
    public void delete(@PathParam("fileId") UUID fileId, @PathParam("userId") Long userId) {
        authService.checkUserIsOwnerOfFile(fileId);
        fileService.deleteAccess(fileId, userId);
    }
}
