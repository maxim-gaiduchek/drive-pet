package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FileDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.FileForm;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UpdateFileDto;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.FileMapper;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.AuthService;
import io.quarkus.security.Authenticated;
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

import java.util.UUID;

@Path("/files")
public class FileController {

    @Inject
    FileService fileService;
    @Inject
    FileMapper fileMapper;
    @Inject
    AuthService authService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public FileDto get(@PathParam("id") UUID id) {
        authService.checkUserHasReadAccessToFile(id);
        var file = fileService.getByIdOrThrow(id);
        return fileMapper.toDto(file);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public FileDto create(@Valid FileForm fileForm) {
        var file = fileService.create(fileForm);
        return fileMapper.toDto(file);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public FileDto update(@PathParam("id") UUID id, @Valid UpdateFileDto fileDto) {
        authService.checkUserHasWriteAccessToFile(id);
        var file = fileService.update(id, fileDto);
        return fileMapper.toDto(file);
    }

    @DELETE
    @Path("/{id}")
    @Authenticated
    public void delete(@PathParam("id") UUID id) {
        authService.checkUserIsOwnerOfFile(id);
        fileService.delete(id);
    }
}
