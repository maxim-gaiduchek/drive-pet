package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FileDto;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.FileMapper;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileService;
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

@Path("/files")
public class FileController {

    @Inject
    FileService fileService;
    @Inject
    FileMapper fileMapper;

    @Deprecated(forRemoval = true)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FileDto get(@PathParam("id") Long id) {
        var file = fileService.getByIdOrThrow(id);
        return fileMapper.toDto(file);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileDto create(@Valid FileDto fileDto) {
        var file = fileService.create(fileDto);
        return fileMapper.toDto(file);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileDto update(@PathParam("id") Long id, @Valid FileDto fileDto) {
        var file = fileService.update(id, fileDto);
        return fileMapper.toDto(file);
    }

    @DELETE
    @Path("/{id}")
    public void update(@PathParam("id") Long id) {
        fileService.delete(id);
    }
}
