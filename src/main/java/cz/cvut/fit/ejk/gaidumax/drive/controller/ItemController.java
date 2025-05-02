package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemSearchDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemDto;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.ItemService;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/items")
public class ItemController {

    @Inject
    ItemService itemService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ItemDto> findAll(@BeanParam ItemSearchDto searchDto) {
        return itemService.findAll(searchDto);
    }
}
