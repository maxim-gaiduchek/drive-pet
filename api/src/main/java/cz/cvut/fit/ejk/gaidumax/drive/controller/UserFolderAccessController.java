package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.dto.UserAccessDto;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.UserFolderAccessMapper;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
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
public class UserFolderAccessController {

    @Inject
    FolderService folderService;
    @Inject
    UserFolderAccessMapper userFolderAccessMapper;
    @Inject
    AuthService authService;

    @GET
    @Path("/folders/{folderId}/accesses")
    @Authenticated
    public List<UserAccessDto> getAllByFolder(@PathParam("folderId") UUID id) {
        var accesses = folderService.getAllAccessesByFolderId(id);
        return userFolderAccessMapper.toDtos(accesses);
    }

    @POST
    @Path("/folders/{folderId}/accesses/users/{userId}")
    @Authenticated
    public UserAccessDto create(@PathParam("folderId") UUID folderId, @PathParam("userId") Long userId,
                                @Valid UserAccessDto userAccessDto) {
        authService.checkUserIsOwnerOfFolder(folderId);
        var access = folderService.createAccess(folderId, userId, userAccessDto);
        return userFolderAccessMapper.toDto(access);
    }

    @PUT
    @Path("/folders/{folderId}/accesses/users/{userId}")
    @Authenticated
    public UserAccessDto update(@PathParam("folderId") UUID folderId, @PathParam("userId") Long userId,
                                @Valid UserAccessDto userAccessDto) {
        authService.checkUserIsOwnerOfFolder(folderId);
        var access = folderService.updateAccess(folderId, userId, userAccessDto);
        return userFolderAccessMapper.toDto(access);
    }

    @DELETE
    @Path("/folders/{folderId}/accesses/users/{userId}")
    @Authenticated
    public void delete(@PathParam("folderId") UUID folderId, @PathParam("userId") Long userId) {
        authService.checkUserIsOwnerOfFolder(folderId);
        folderService.deleteAccess(folderId, userId);
    }
}
