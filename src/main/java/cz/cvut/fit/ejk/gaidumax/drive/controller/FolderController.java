package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FolderDto;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.FolderMapper;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

@Path("/folders")
public class FolderController {

    @Inject
    FolderService folderService;
    @Inject
    FolderMapper folderMapper;

    @Deprecated(forRemoval = true)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FolderDto get(@PathParam("id") UUID id) {
        var folder = folderService.getByIdOrThrow(id);
        return folderMapper.toDto(folder);
    }

    @GET
    @Path("/{id}/parents")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FolderDto> getAllParentFolder(@PathParam("id") UUID id) {
        var folders = folderService.getAllParentFolders(id);
        return folderMapper.toDtos(folders);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FolderDto create(@Valid FolderDto folderDto) {
        var folder = folderService.create(folderDto);
        return folderMapper.toDto(folder);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FolderDto update(@PathParam("id") UUID id, @Valid FolderDto folderDto) {
        var folder = folderService.update(id, folderDto);
        return folderMapper.toDto(folder);
    }

    @DELETE
    @Path("/{id}")
    public void update(@PathParam("id") UUID id) {
        folderService.delete(id);
    }
}
